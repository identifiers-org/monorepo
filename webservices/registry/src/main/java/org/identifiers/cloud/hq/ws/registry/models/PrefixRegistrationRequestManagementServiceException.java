package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-15 10:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class PrefixRegistrationRequestManagementServiceException extends RuntimeException {
    public PrefixRegistrationRequestManagementServiceException(String message) {
        super(message);
    }
    public PrefixRegistrationRequestManagementServiceException(String message, Throwable e) {
        super(message, e);
    }
}
