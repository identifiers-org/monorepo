package org.identifiers.cloud.ws.linkchecker.data.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-25 4:36
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class LinkCheckRequest implements Serializable, Comparable<LinkCheckRequest> {
    // URL that has been checked
    protected String url;
    // When it has been checked (UTC)
    protected Timestamp requestTimestamp;
    // Link check request type / reference
    protected String providerId;
    protected String resourceId;

    public String getUrl() {
        return url;
    }

    public LinkCheckRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getRequestTimestamp() {
        return requestTimestamp;
    }

    public LinkCheckRequest setRequestTimestamp(Timestamp requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
        return this;
    }

    public String getProviderId() {
        return providerId;
    }

    public LinkCheckRequest setProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public LinkCheckRequest setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    @Override
    public int compareTo(LinkCheckRequest o) {
        return this.requestTimestamp.compareTo(o.getRequestTimestamp());
    }

}
