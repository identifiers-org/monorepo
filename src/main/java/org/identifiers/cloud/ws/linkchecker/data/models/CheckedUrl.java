package org.identifiers.cloud.ws.linkchecker.data.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 10:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a checked URL
 */
public class CheckedUrl implements Serializable, Comparable<CheckedUrl> {
    // URL that has been checked
    private String url;
    // When it has been checked (UTC)
    private Timestamp timestamp;
    // Returning HTTP Status code
    private int httpStatus;

    public String getUrl() {
        return url;
    }

    public CheckedUrl setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public CheckedUrl setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public CheckedUrl setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    @Override
    public int compareTo(CheckedUrl o) {
        return this.timestamp.compareTo(o.getTimestamp());
    }
}
