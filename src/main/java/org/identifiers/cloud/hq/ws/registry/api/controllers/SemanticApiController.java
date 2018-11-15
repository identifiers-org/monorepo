package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.SemanticApiModel;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseSemanticExportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2018-11-15 11:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("semanticApi")
public class SemanticApiController {
    @Autowired
    private SemanticApiModel model;

    @RequestMapping(value = "getRegistryOntology", method = RequestMethod.GET)
    public ResponseEntity<?> getRegistryOntology() {
        ServiceResponseSemanticExportRequest response = model.getRegistryOntology();
        return new ResponseEntity<>(response.getPayload(), response.getHttpStatus());
    }
}
