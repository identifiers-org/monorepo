package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.api.models.CuratingWarningModel;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarning;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarningEvent;
import org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings.CurationWarningRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curationApi")
@RequiredArgsConstructor
public class CurationController {
    private final CuratingWarningModel curatingWarningModel;
    private final CurationWarningRepository curationWarningRepository;
    private final PagedResourcesAssembler<CurationWarning> pagedAssembler;
    private final EntityLinks entityLinks;

    @PostMapping("/notifications")
    public void receiveNotifications(@RequestBody ServiceRequest<List<CurationWarningNotification>> notifications) {
        for (var notification : notifications.getPayload()) {
            if (notification == null) continue;

            curatingWarningModel.updateCurationWarningWithNotification(notification);
        }
    }

    @PatchMapping("/snooze")
    public ResponseEntity<Void> snoozeCurationWarning(@RequestBody ServiceRequest<Long> request) {
        Long id = request.getPayload();
        if (id == null) return ResponseEntity.badRequest().build();

        return curatingWarningModel.addNewEventToOpenCurationWarning(id, CurationWarningEvent.Type.SNOOZED) ?
            ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/markSolved")
    public ResponseEntity<Void> markCurationWarningAsSolved(@RequestBody ServiceRequest<Long> request) {
        Long id = request.getPayload();
        if (id == null) return ResponseEntity.badRequest().build();

        return curatingWarningModel.addNewEventToOpenCurationWarning(id, CurationWarningEvent.Type.SOLVED) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/queryWarnings")
    public PagedModel<?> query(@RequestParam(defaultValue = "all") String targetType,
                               @RequestParam(required = false) String warningType,
                               @RequestParam(defaultValue = "false") Boolean includeClosed,
                               Pageable pageable) {
        var values = curatingWarningModel.getExampleCurationWarningFor(targetType)
                .setType(warningType).setOpen(true);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase().withIgnoreNullValues().withIgnorePaths("id")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
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

    @GetMapping("/getWarningTypesByTargetType")
    public ServiceResponse<List<String>> getWarningTypes(@RequestParam(defaultValue = "all") String type) {
        return ServiceResponse.of(curatingWarningModel.getAllCurationWarnings(type));
    }
}
