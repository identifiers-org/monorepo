package org.identifiers.cloud.ws.resolver.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 11:35
 * ---
 *
 * This may be the main Resolver API response object, let's see how it evolves over time, as the Resolver WS is being
 * build
 */
@JsonIgnoreProperties(value = {"httpStatus"})
public class ResolverApiResponse implements Serializable {
    private String errorMsg;
    private HttpStatus httpStatus = HttpStatus.OK;
    private List<ResolverApiResponseResource> resolvedResources = new ArrayList<>();

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<ResolverApiResponseResource> getResolvedResources() {
        return resolvedResources;
    }

    public void setResolvedResources(List<ResolverApiResponseResource> resolvedResources) {
        this.resolvedResources = resolvedResources;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
