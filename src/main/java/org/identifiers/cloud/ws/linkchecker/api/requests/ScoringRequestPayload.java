package org.identifiers.cloud.ws.linkchecker.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.requests
 * Timestamp: 2018-05-26 10:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoringRequestPayload implements Serializable {
    protected String url;

    public String getUrl() {
        return url;
    }

    public ScoringRequestPayload setUrl(String url) {
        this.url = url;
        return this;
    }
}
