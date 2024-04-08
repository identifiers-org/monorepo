package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 01:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ResourceRegistrationRequestManagementServiceException extends RuntimeException {
    public ResourceRegistrationRequestManagementServiceException(String message) {
        super(message);
    }
    public ResourceRegistrationRequestManagementServiceException(String message, Throwable e) {
        super(message, e);
    }
}
