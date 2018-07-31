package org.identifiers.cloud.ws.linkchecker.api.responses;

import java.io.Serializable;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.responses
 * Timestamp: 2018-07-31 11:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a generic payload for management requests.
 */
public class ServiceResponseManagementRequestPayload implements Serializable {
    private String message = "";

    public String getMessage() {
        return message;
    }

    public ServiceResponseManagementRequestPayload setMessage(String message) {
        this.message = message;
        return this;
    }
}
