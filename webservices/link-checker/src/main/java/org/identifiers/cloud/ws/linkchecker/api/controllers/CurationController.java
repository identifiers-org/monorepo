package org.identifiers.cloud.ws.linkchecker.api.controllers;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseResourceAvailabilityPayload;
import org.identifiers.cloud.ws.linkchecker.api.models.LinkScoringApiModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * This is a temporary feature while we don't have a dedicated curation pipeline
 */
@RestController
@RequiredArgsConstructor
public class CurationController {
    private final LinkScoringApiModel model;

    @GetMapping("/getResourcesWithLowAvailability")
    public ResponseEntity<ServiceResponse<ServiceResponseResourceAvailabilityPayload>> getResourcesWithLowAvailability(
            @RequestParam(defaultValue="80", required=false) int minAvailability)
    {
        var response = model.getResourcesIdsWithAvailabilityLowerThan(minAvailability);
        if (response.getPayload().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
