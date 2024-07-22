package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.ws.registry.api.models.ResolutionApiModel;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2018-10-16 12:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("resolutionApi")
@RequiredArgsConstructor
public class ResolutionApiController {
    private final ResolutionApiModel model;

    @GetMapping(value = "/getResolverDataset")
    public ResponseEntity<?> getResolverDataset(
            @RequestParam(defaultValue = "false")
            boolean rewriteForEmbeddedPrefixes
    ) {
        ServiceResponseGetResolverDataset response = model.getResolverDataset(rewriteForEmbeddedPrefixes);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
