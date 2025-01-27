package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.SchemaOrgApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-17 06:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("schemaOrgApi")
public class SchemaOrgApiController {
    // TODO
    @Autowired
    private SchemaOrgApiModel model;

    @GetMapping("getMetadataForPlatform")
    public ResponseEntity<?> getMetadataForPlatform() {
        return model.getMetadataForPlatform();
    }

    @GetMapping("getMetadataForNamespace/{id}")
    public ResponseEntity<?> getMetadataForNamespace(@PathVariable long id) {
        // TODO
        return model.getMetadataForNamespace(id);
    }

    @GetMapping("getMetadataForNamespacePrefix/{namespacePrefix}")
    public ResponseEntity<?> getMetadataForNamespacePrefix(@PathVariable String namespacePrefix) {
        // TODO
        return model.getMetadataForNamespacePrefix(namespacePrefix);
    }
}
