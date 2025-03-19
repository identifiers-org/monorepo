package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.WarningsSummaryPayload;
import org.identifiers.cloud.hq.ws.registry.api.models.CuratingWarningModel;
import org.identifiers.cloud.hq.ws.registry.api.models.UsageScoreHelperBasedOnMatomo;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarning;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.CurationWarningRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/curationApi")
@RequiredArgsConstructor
public class CurationController {
    private final CuratingWarningModel curatingWarningModel;
    private final CurationWarningRepository curationWarningRepository;
    private final PagedResourcesAssembler<CurationWarning> pagedAssembler;
    private final EntityLinks entityLinks;
    private final Optional<UsageScoreHelperBasedOnMatomo> usageScorer;

    @PostMapping("/notifications")
    public void receiveNotifications(@RequestBody ServiceRequest<List<CurationWarningNotification>> notifications) {
        if (notifications.getPayload() == null) return;

        usageScorer.ifPresent(UsageScoreHelperBasedOnMatomo::downloadDataset);

        log.debug("Processing {} curation warning notifications", notifications.getPayload().size());
        for (var notification : notifications.getPayload()) {
            if (notification == null) continue;

            curatingWarningModel.updateCurationWarningWithNotification(notification);
        }

        var notifiedGlobalIds = notifications.getPayload().stream()
                .map(CurationWarningNotification::getGlobalId).toList();
        curatingWarningModel.updateStaleCurationWarnings(notifiedGlobalIds);

        log.debug("Finished processing notifications");
    }

    @GetMapping("/queryWarnings")
    public PagedModel<?> query(@RequestParam(defaultValue = "all") String targetType,
                               @RequestParam(defaultValue = "all") String warningType,
                               @RequestParam(defaultValue = "false") Boolean includeClosed,
                               Pageable pageable) {
        var values = curatingWarningModel.getExampleCurationWarningFor(targetType)
                .setType(warningType.equals("all") ? null : warningType).setOpen(true);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase().withIgnoreNullValues().withIgnorePaths("id");
        if (includeClosed) matcher = matcher.withIgnorePaths("open");

        Example<CurationWarning> example = Example.of(values, matcher);
        Page<CurationWarning> result = curationWarningRepository.findAll(example, pageable);

        var models = pagedAssembler.toModel(result);
        for (var pm : models) {
            var cw = pm.getContent();
            if (cw == null) continue;

            var selfLink = entityLinks.linkForItemResource(cw, CurationWarning::getId).withSelfRel();
            var targetLink = entityLinks.linkForItemResource(cw, CurationWarning::getId)
                    .slash("target").withRel("target");
            var eventsLink = entityLinks.linkForItemResource(cw, CurationWarning::getId)
                    .slash("events").withRel("events");
            pm.add(selfLink, targetLink, eventsLink);
        }
        return models;
    }

    @GetMapping("/warningsSummary")
    public ResponseEntity<ServiceResponse<WarningsSummaryPayload>> getWarningSummary() {
        var openWarnings = curationWarningRepository.findAllByOpenTrue();
        var table = curatingWarningModel.getSummaryPayload(openWarnings);

        var serviceResponse = ServiceResponse.of(table);
        var cacheControl = CacheControl.maxAge(Duration.ofMinutes(1)).noTransform().mustRevalidate();
        return ResponseEntity.ok().cacheControl(cacheControl).body(serviceResponse);
    }
}
