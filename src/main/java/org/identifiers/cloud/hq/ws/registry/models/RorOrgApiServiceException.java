package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-15 01:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class RorOrgApiServiceException extends RuntimeException {
    public RorOrgApiServiceException(String message) {
        super(message);
    }
    public RorOrgApiServiceException(String message, Throwable e) {
        super(message, e);
    }
}
