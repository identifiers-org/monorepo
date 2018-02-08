package org.identifiers.org.cloud.ws.metadata.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:36
 * ---
 */
@JsonIgnoreProperties(value = {"httpStatus"})
public class MetadataApiResponse implements Serializable {

    private HttpStatus httpStatus = HttpStatus.OK;
    private String errorMessage;
    // Right now we focus on JSON-LD formatted metadata and we're not doing anything with it, so I keep it as a String
    private String metadata;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public MetadataApiResponse setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public MetadataApiResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getMetadata() {
        return metadata;
    }

    public MetadataApiResponse setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }
}
