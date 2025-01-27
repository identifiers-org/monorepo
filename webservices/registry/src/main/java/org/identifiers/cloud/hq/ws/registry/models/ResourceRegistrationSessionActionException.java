package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 11:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ResourceRegistrationSessionActionException extends RuntimeException {
    public ResourceRegistrationSessionActionException(String message) {
        super(message);
    }
    public ResourceRegistrationSessionActionException(String message, Throwable e) {
        super(message, e);
    }
}
