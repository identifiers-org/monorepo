package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.NamespaceManagementApiModel;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseDeactivateNamespace;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseReactivateNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-01 20:51
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("namespaceManagement")
public class NamespaceManagementApiController {
    @Autowired
    private NamespaceManagementApiModel model;

    // --- Namespace Lifecycle Management
    @GetMapping(value = "/deactivateNamespace/{namespaceId}")
    public ResponseEntity<?> deactivateNamespace(@PathVariable long namespaceId) {
        ServiceResponseDeactivateNamespace response = model.deactivateNamespace(namespaceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/reactivateNamespace/{namespaceId}")
    public ResponseEntity<?> reactivateNamespace(@PathVariable long namespaceId) {
        ServiceResponseReactivateNamespace response = model.reactivateNamespace(namespaceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
