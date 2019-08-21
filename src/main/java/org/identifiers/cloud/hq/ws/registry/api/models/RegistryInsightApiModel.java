package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-21 02:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class RegistryInsightApiModel {
    @Autowired
    private NamespaceService namespaceService;

    public ResponseEntity<?> getAllNamespacePrefixes() {
        return new ResponseEntity<>(namespaceService.getAllNamespacePrefixes(), HttpStatus.OK);
    }
}
