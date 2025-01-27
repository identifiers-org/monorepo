package org.identifiers.cloud.hq.ws.registry.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.responses
 * Timestamp: 2018-10-10 13:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ServiceResponse<T> implements Serializable {
    private String apiVersion;
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // payload
    private T payload;

    public String getApiVersion() {
        return apiVersion;
    }

    public ServiceResponse setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServiceResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ServiceResponse setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public ServiceResponse setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public static <I> ServiceResponse<I> getBaseResponse(I payload) {
        ServiceResponse<I> defaultResponse = new ServiceResponse<>();
        defaultResponse.setApiVersion(ApiCentral.apiVersion);
        defaultResponse.setPayload(payload);
        return defaultResponse;
    }

    public static ServiceResponse getBaseResponse() {
        ServiceResponse defaultResponse = new ServiceResponse<>();
        defaultResponse.setApiVersion(ApiCentral.apiVersion);
        return defaultResponse;
    }
}