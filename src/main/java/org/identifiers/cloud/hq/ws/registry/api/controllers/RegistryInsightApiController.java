package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.RegistryInsightApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-21 02:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("registryInsightApi")
public class RegistryInsightApiController {
    // TODO
    @Autowired
    private RegistryInsightApiModel model;

    @GetMapping("/getAllNamespacePrefixes")
    public ResponseEntity<?> getAllNamespacePrefixes() {
        return model.getAllNamespacePrefixes();
    }
}
