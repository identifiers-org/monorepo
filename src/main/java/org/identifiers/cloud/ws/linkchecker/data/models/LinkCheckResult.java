package org.identifiers.cloud.ws.linkchecker.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 10:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a checked URL, for either a provider, a resource or a plain URL.
 */
@RedisHash(value = "LinkCheckerLinkCheckResult")
public class LinkCheckResult implements Serializable, Comparable<LinkCheckResult> {
    // TODO - We'll let Redis to create an ID for this entity
    // Result ID, hopefully manufactured by Redis
    @Id
    private String id;
    // URL that has been checked
    @Indexed
    private String url;
    // When it has been checked (UTC), indexed to easily find those entities to remove
    @Indexed
    private String timestamp = (new Timestamp(new Date().getTime())).toString();
    // When this check was requested (UTC)
    private String requestTimestamp;
    // Link check request type / reference
    @Indexed
    private String providerId;
    @Indexed
    private String resourceId;
    // Returning HTTP Status code
    private int httpStatus;
    // Checking strategy URL status evaluation. This is an assessment of the checked URL, on whether it is considered to
    // lead to a non-error resource, and it is calculated by the link checking strategy used.
    private boolean urlAssessmentOk = false;

    public String getId() {
        return id;
    }

    public LinkCheckResult setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public LinkCheckResult setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getTimestamp() {
        return Timestamp.valueOf(timestamp);
    }

    public LinkCheckResult setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp.toString();
        return this;
    }

    public Timestamp getRequestTimestamp() {
        return Timestamp.valueOf(requestTimestamp);
    }

    public LinkCheckResult setRequestTimestamp(Timestamp requestTimestamp) {
        this.requestTimestamp = requestTimestamp.toString();
        return this;
    }

    public String getProviderId() {
        return providerId;
    }

    public LinkCheckResult setProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public String getResourceId() {
        return resourceId;
    }

    public LinkCheckResult setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public LinkCheckResult setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public boolean isUrlAssessmentOk() {
        return urlAssessmentOk;
    }

    public LinkCheckResult setUrlAssessmentOk(boolean urlAssessmentOk) {
        this.urlAssessmentOk = urlAssessmentOk;
        return this;
    }

    @Override
    public int compareTo(LinkCheckResult o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
