package org.identifiers.cloud.ws.resolver.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:33
 * ---
 */
// TODO - This will be externalized later in a separated library with models and service wrappers for all identifiers.org services
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ResourceRecommenderResponse implements Serializable {
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // payload
    private List<RecommendedResource> payload;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ResourceRecommenderResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ResourceRecommenderResponse setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public List<RecommendedResource> getPayload() {
        return payload;
    }

    public ResourceRecommenderResponse setPayload(List<RecommendedResource> payload) {
        this.payload = payload;
        return this;
    }
}
