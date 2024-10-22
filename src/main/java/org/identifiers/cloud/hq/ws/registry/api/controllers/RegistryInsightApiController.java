package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.models.RegistryInsightApiModel;
import org.identifiers.cloud.hq.ws.registry.models.LowAvailabilityOnLinkCheckerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-21 02:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("registryInsightApi")
public class RegistryInsightApiController {
    private final RegistryInsightApiModel insightModel;
    private final Optional<LowAvailabilityOnLinkCheckerService> lowAvailabilityOnLinkCheckerService;

    @GetMapping("/getAllNamespacePrefixes")
    public ResponseEntity<?> getAllNamespacePrefixes() {
        return insightModel.getAllNamespacePrefixes();
    }

    @GetMapping("/getEbiSearchDataset")
    public ResponseEntity<ExportedDocument> getEbiSearchDataset(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start) {
        log.debug("Ebi search request for entries updates since {}", start);
        ExportedDocument export = insightModel.getEbiSearchExport(start);
        if (export != null)
            return new ResponseEntity<>(export, HttpStatus.OK);
        else
            return ResponseEntity.noContent().build();
    }

    /**
     * This is meant to be a temporary API to be replaced when an actual curation service is deployed
     */
    @GetMapping("/getResourcesWithLowAvailability")
    public ResponseEntity<Map<String, Float>> getResourcesWithLowAvailability() {
        if (lowAvailabilityOnLinkCheckerService.isEmpty()) return ResponseEntity.noContent().build();

        var responseBody = lowAvailabilityOnLinkCheckerService.get().getResourcesToAvailabilityMap();
        if (responseBody != null && !responseBody.isEmpty()) {
            return ResponseEntity.ok((responseBody));
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
