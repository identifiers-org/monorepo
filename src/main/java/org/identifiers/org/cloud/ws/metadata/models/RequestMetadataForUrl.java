package org.identifiers.org.cloud.ws.metadata.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-17 20:27
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestMetadataForUrl {
    private String url;

    public String getUrl() {
        return url;
    }

    public RequestMetadataForUrl setUrl(String url) {
        this.url = url;
        return this;
    }
}
