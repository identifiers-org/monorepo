package org.identifiers.org.cloud.ws.metadata.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-17 20:27
 * ---
 */
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
