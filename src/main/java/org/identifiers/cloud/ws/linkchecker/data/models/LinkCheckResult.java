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
 * This entity models a checked URL, for either a provider, a resource or a plain URL.
 */
public class LinkCheckResult implements Serializable, Comparable<LinkCheckResult> {
    // When it has been checked (UTC)
    private Timestamp checkTimestamp;
    // Returning HTTP Status code
    private int httpStatus;

    public Timestamp getCheckTimestamp() {
        return checkTimestamp;
    }

    public LinkCheckResult setCheckTimestamp(Timestamp checkTimestamp) {
        this.checkTimestamp = checkTimestamp;
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public LinkCheckResult setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    @Override
    public int compareTo(LinkCheckResult o) {
        return this.checkTimestamp.compareTo(o.getCheckTimestamp());
    }
}
