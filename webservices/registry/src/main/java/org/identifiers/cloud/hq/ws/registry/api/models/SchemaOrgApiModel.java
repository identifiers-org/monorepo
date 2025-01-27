package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProvider;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-17 06:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class SchemaOrgApiModel {
    @Autowired
    private SchemaOrgMetadataProvider schemaOrgMetadataProvider;

    public ResponseEntity<?> getMetadataForPlatform() {
        try {
            return new ResponseEntity<>(schemaOrgMetadataProvider.getForPlatform(), HttpStatus.OK);
        } catch (SchemaOrgMetadataProviderException e) {
            String errorMessage = String.format("Could not produce platform metadata due to '%s'", e.getMessage());
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getMetadataForNamespace(long namespaceId) {
        try {
            return new ResponseEntity<>(schemaOrgMetadataProvider.getForNamespace(namespaceId), HttpStatus.OK);
        } catch (SchemaOrgMetadataProviderException e) {
            String errorMessage = String.format("Could not produce metadata for namespace ID '%d' due to '%s'", namespaceId, e.getMessage());
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getMetadataForNamespacePrefix(String namespacePrefix) {
        try {
            return new ResponseEntity<>(schemaOrgMetadataProvider.getForNamespacePrefix(namespacePrefix.toLowerCase()), HttpStatus.OK);
        } catch (SchemaOrgMetadataProviderException e) {
            String errorMessage = String.format("Could not produce metadata for namespace with prefix '%s' due to '%s'", namespacePrefix, e.getMessage());
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}
