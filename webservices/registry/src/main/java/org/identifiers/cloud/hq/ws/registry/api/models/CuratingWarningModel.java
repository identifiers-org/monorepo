package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.responses.registry.WarningsSummaryPayload;
import org.identifiers.cloud.commons.messages.responses.registry.WarningsSummaryPayload.TargetInfo;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.*;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.*;
import org.identifiers.cloud.hq.ws.registry.data.repositories.InstitutionRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarningEvent.Type.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CuratingWarningModel {
    private final CurationWarningRepository curationWarningRepository;
    private final InstitutionRepository institutionRepository;
    private final ResourceRepository resourceRepository;
    private final NamespaceRepository namespaceRepository;
    private final TransactionTemplate transactionTemplate;
    private final CurationWarningEventsRepository curationWarningEventsRepository;
    private final Optional<UsageScoreHelperBasedOnMatomo> usageScorer;

    public void updateCurationWarningWithNotification(CurationWarningNotification notification) {
        Objects.requireNonNull(notification);

        var warningOpt = curationWarningRepository.findByGlobalId(notification.getGlobalId());
        var details = notification.getDetails().entrySet().stream()
                .map(CuratingWarningModel::getDetailFrom)
                .collect(Collectors.toSet());

        CurationWarning warning = warningOpt.orElseGet(() -> newCurationWarningFor(notification));
        if (warning == null || !StringUtils.equals(warning.getType(), notification.getType())) {
            // Warning is null when notification target information is invalid
            //  type equality check for possible wrong use of global ID
            return;
        }

        details.forEach(d -> d.setCurationWarning(warning));
        warning.setLastNotification(new Date()); // Current date

        updateLatestEventWithNotification(warning);

        try {
            transactionTemplate.executeWithoutResult(s -> {
                warning.getDetails().clear();
                warning.getDetails().addAll(details);
                curationWarningRepository.save(warning);
            });
        } catch (TransactionException e) {
            log.error("Failed to save notification: {}", e.getMessage());
            log.debug("Stacktrace:", e);
        }
    }

    private void updateLatestEventWithNotification(CurationWarning warning) {

        CurationWarningEvent.Type newEventType = null;
        if (warning.getLatestEvent() == null) {
            newEventType = CREATED;
        } else if (warning.getLatestEvent().getType() == SOLVED) {
            newEventType = REOPENED;
        }

        // null event type when warning is open and not new
        if (newEventType != null) {
            var newEvent = new CurationWarningEvent()
                    .setType(newEventType)
                    .setCurationWarning(warning);
            warning.getEvents().add(newEvent);
            curationWarningRepository.save(warning);
        }
    }

    public HttpStatus markCurationAsDisabled(long warningId) {
        var warningOpt = curationWarningRepository.findById(warningId);
        if (warningOpt.isEmpty())
            return HttpStatus.NOT_FOUND;

        var warning = warningOpt.get();
        var lastEvent = warning.getLatestEvent();
        var invalidStatusLst = List.of(DISABLED, SOLVED);
        if (invalidStatusLst.contains(lastEvent.getType())) {
            return HttpStatus.BAD_REQUEST;
        }

        var warningEvents = warning.getEvents();
        var newEvent = new CurationWarningEvent()
                .setType(DISABLED)
                .setCurationWarning(warning);
        warningEvents.add(newEvent);
        curationWarningRepository.save(warning);
        return HttpStatus.OK;
    }

    public HttpStatus markCurationAsEnabled(long warningId) {
        var warningOpt = curationWarningRepository.findById(warningId);
        if (warningOpt.isEmpty())
            return HttpStatus.NOT_FOUND;

        var warning = warningOpt.get();
        var lastEvent = warning.getLatestEvent();
        if (!DISABLED.equals(lastEvent.getType())) {
            return HttpStatus.BAD_REQUEST;
        }

        var warningEvents = warning.getEvents();
        var newEvent = new CurationWarningEvent()
                .setType(REOPENED)
                .setCurationWarning(warning);
        warningEvents.add(newEvent);
        curationWarningRepository.save(warning);
        return HttpStatus.OK;
    }

    public CurationWarning getExampleCurationWarningFor(String targetType) {
        return switch(targetType) {
            case "institution" -> new InstitutionCurationWarning();
            case "resource" -> new ResourceCurationWarning();
            case "namespace" -> new NamespaceCurationWarning();
            default -> new CurationWarning();
        };
    }

    /**
     * @param notification Notification to create new warning from.
     * @return new CurationWarning of the correct type and populated with correct information. Null if target type and ID is invalid
     */
    @Nullable
    private CurationWarning newCurationWarningFor(CurationWarningNotification notification) {
        CurationWarning warning = null;

        var type = notification.getTargetType();
        if (StringUtils.equals(type, "institution")) {
            var institution = institutionRepository.findById(notification.getTargetId());
            if (institution.isPresent()) {
                warning = new InstitutionCurationWarning().setInstitution(institution.get());
            }
        } else if (StringUtils.equals(type, "namespace")) {
            var namespace = namespaceRepository.findById(notification.getTargetId());
            if (namespace.isPresent()) {
                warning = new NamespaceCurationWarning().setNamespace(namespace.get());
            }
        } else if (StringUtils.equals(type, "resource")) {
            var resource = resourceRepository.findById(notification.getTargetId());
            if (resource.isPresent()) {
                warning = new ResourceCurationWarning().setResource(resource.get());
            }
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + type);
        }
        if (warning == null) return null; // Invalid TARGET-TYPE, TARGET-ID pair

        warning.setGlobalId(notification.getGlobalId());
        warning.setType(notification.getType());
        warning.setDetails(new HashSet<>());
        warning.setEvents(new LinkedList<>());
        return warning;
    }

    private static CurationWarningDetail getDetailFrom(Map.Entry<String, String> entry) {
        return new CurationWarningDetail()
                .setLabel(entry.getKey())
                .setValue(entry.getValue());
    }

    public void updateStaleCurationWarnings(List<String> notifiedGlobalIds) {
        var staleCurationWarnings = curationWarningRepository.findByGlobalIdNotInAndOpenTrue(notifiedGlobalIds);
        log.info("Marking {} warnings as stale", staleCurationWarnings.size());

        for (var curationWarning : staleCurationWarnings) {
            var newEvent = new CurationWarningEvent()
                    .setType(SOLVED)
                    .setCurationWarning(curationWarning);
            curationWarningEventsRepository.save(newEvent);
        }
    }

    public WarningsSummaryPayload getSummaryPayload(List<CurationWarning> openWarnings) {
        Map<TargetInfo, List<CurationWarning>> groupedWarnings = openWarnings.stream()
                .collect(Collectors.groupingBy(CuratingWarningModel::targetInfo));

        var payload = new WarningsSummaryPayload();
        var rows = groupedWarnings.entrySet().stream()
                .map(e -> getSummaryEntry(e.getKey(), e.getValue()))
                .toList();
        payload.setSummaryEntries(rows);

        Map<String, Long> scores = usageScorer
                .map(UsageScoreHelperBasedOnMatomo::getScoresPerNamespace)
                .orElseGet(Map::of);
        payload.setNamespaceUsage(scores);

        return payload;
    }

    private static WarningsSummaryPayload.Entry getSummaryEntry(TargetInfo targetInfo,
                                                                List<CurationWarning> curationWarnings) {
        int failingInstitutionUrl = 0;
        int lowAvailabilityResources = 0;
        boolean hasCurationValues = false;
        boolean hasPossibleWikidataError = false;

        boolean allDisabled = true;
        for (var cw : curationWarnings) {
            if (cw.isOpen() && cw.getLatestEvent().getType() != DISABLED) {
                allDisabled = false;
            }

            switch (cw.getType()) {
                case "wikidata-institution-diff":
                    hasPossibleWikidataError = true;
                    break;
                case "curator-review":
                    hasCurationValues = true;
                    break;
                case "low-availability-resource":
                    lowAvailabilityResources++;
                    break;
                case "url-not-ok":
                    failingInstitutionUrl++;
                    break;
                default:
                    log.warn("Unsupported curation warning type: {}", cw.getType());
            }
        }
        var row = new WarningsSummaryPayload.Entry();
        row.setTargetInfo(targetInfo);
        row.setFailingInstitutionUrl(failingInstitutionUrl);
        row.setLowAvailabilityResources(lowAvailabilityResources);
        row.setHasCurationValues(hasCurationValues);
        row.setHasPossibleWikidataError(hasPossibleWikidataError);
        row.setAllDisabled(allDisabled);
        return row;
    }

    private static TargetInfo targetInfo(CurationWarning curationWarning) {
        var info = new TargetInfo();
        if (curationWarning instanceof InstitutionCurationWarning icw) {
            return info.setIdentifier(String.valueOf(icw.getInstitution().getId()))
                    .setType("Institution")
                    .setLabel(icw.getInstitution().getName());
        } else if (curationWarning instanceof NamespaceCurationWarning nwc) {
            return info.setIdentifier(String.valueOf(nwc.getNamespace().getPrefix()))
                    .setType("Prefix")
                    .setLabel(nwc.getNamespace().getName());
        } else if (curationWarning instanceof ResourceCurationWarning rwc) {
            return info.setIdentifier(String.valueOf(rwc.getResource().getNamespace().getPrefix()))
                    .setType("Prefix")
                    .setLabel(rwc.getResource().getNamespace().getName());
        } else {
            throw new IllegalArgumentException("Unsupported curation warning type: " + curationWarning.getType());
        }
    }
}
