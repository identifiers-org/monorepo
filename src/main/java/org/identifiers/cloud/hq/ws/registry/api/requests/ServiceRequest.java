package org.identifiers.cloud.hq.ws.registry.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.requests
 * Timestamp: 2018-10-10 13:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequest<T> implements Serializable {
    // TODO - I may go for the option of versioning the API via the URL - REMOVE THIS
    private String apiVersion;
    private T payload;

    public String getApiVersion() {
        return apiVersion;
    }

    public ServiceRequest setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public ServiceRequest setPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
