package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 18:41
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class NamespaceLifecycleManagementServiceException extends RuntimeException {
    public NamespaceLifecycleManagementServiceException(String message) {
        super(message);
    }
    public NamespaceLifecycleManagementServiceException(String message, Throwable e) {
        super(message, e);
    }
}
