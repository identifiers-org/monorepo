package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.*;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.CurationWarningRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.InstitutionRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.InstitutionCurationWarningRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.NamespaceCurationWarningRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.ResourceCurationWarningRepository;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarningEvent.Type.*;

@Component
@RequiredArgsConstructor
public class CuratingWarningModel {
    private final CurationWarningRepository curationWarningRepository;
    private final NamespaceCurationWarningRepository namespaceCurationWarningRepository;
    private final ResourceCurationWarningRepository resourceCurationWarningRepository;
    private final InstitutionCurationWarningRepository institutionCurationWarningRepository;
    private final InstitutionRepository institutionRepository;
    private final ResourceRepository resourceRepository;
    private final NamespaceRepository namespaceRepository;
    private final TransactionTemplate transactionTemplate;
    private final AuthHelper authHelper;

    public List<String> getAllCurationWarnings(String type) {
        return switch(type) {
            case("all") -> curationWarningRepository.findAllDistinctTypes();
            case("resource") -> resourceCurationWarningRepository.findAllDistinctTypes();
            case("institution") -> institutionCurationWarningRepository.findAllDistinctTypes();
            case("namespace") -> namespaceCurationWarningRepository.findAllDistinctTypes();
            default -> List.of();
        };
    }

    /**
     * @param id ID of open warning to create event for
     * @param newEventType Type of event to be created
     * @return True if event was created successfully
     */
    public boolean addNewEventToOpenCurationWarning(long id, CurationWarningEvent.Type newEventType) {
        var warningOpt = curationWarningRepository.findById(id);
        if (warningOpt.isPresent()) {
            var warning = warningOpt.get();
            var actor = authHelper.getCurrentUsername();

            if (warning.isOpen()) {
                var newEvent = new CurationWarningEvent()
                        .setType(newEventType)
                        .setCurationWarning(warning)
                        .setActor(actor);
                warning.getEvents().add(newEvent);
                curationWarningRepository.save(warning);
                return true;
            }
        }
        return false;
    }

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

        updateLatestEvents(warning);

        transactionTemplate.executeWithoutResult(s -> {
            warning.getDetails().clear();
            warning.getDetails().addAll(details);
            curationWarningRepository.save(warning);
        });
    }

    private void updateLatestEvents(CurationWarning warning) {
        var actor = authHelper.getCurrentUsername();

        CurationWarningEvent.Type newEventType = null;
        if (warning.getLatestEvent() == null) {
            newEventType = CREATED;
        } else if (warning.getLatestEvent().getType() == SOLVED) {
            newEventType = REOPENED;
        }

        // null event type when warning is open and not new
        if (newEventType != null) {
            var newEvent = new CurationWarningEvent()
                    .setType(CREATED)
                    .setCurationWarning(warning)
                    .setActor(actor);
            warning.getEvents().add(newEvent);
            curationWarningRepository.save(warning);
        }
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
}
