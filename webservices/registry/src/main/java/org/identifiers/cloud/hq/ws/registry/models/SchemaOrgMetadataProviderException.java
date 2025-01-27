package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-19 10:31
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class SchemaOrgMetadataProviderException extends RuntimeException {
    public SchemaOrgMetadataProviderException(String message) {
        super(message);
    }
    public SchemaOrgMetadataProviderException(String message, Throwable e) {
        super(message, e);
    }
}
