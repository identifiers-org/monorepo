package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 05:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ResourceLifecycleManagementServiceException extends RuntimeException {
    public ResourceLifecycleManagementServiceException(String message) {
        super(message);
    }
    public ResourceLifecycleManagementServiceException(String message, Throwable e) {
        super(message, e);
    }
}
