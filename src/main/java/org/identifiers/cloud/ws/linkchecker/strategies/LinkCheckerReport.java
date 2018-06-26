package org.identifiers.cloud.ws.linkchecker.strategies;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Report built by a link checking strategy.
 */
public class LinkCheckerReport implements Serializable {
    // Checked URL
    private String url;
    // UTC time stamp when the URL was checked
    private Timestamp timestamp;
    // Returned HTTP Status
    private int httpStatus;
    // Checking strategy URL status evaluation. This is an assessment of the checked URL, on whether it is considered to
    // lead to a non-error resource, and it is calculated by the link checking strategy used.
    private boolean urlAssessmentOk = false;
    // TODO - Do we want to add additional information here, e.g. HTTP Redirection destinations?

    public String getUrl() {
        return url;
    }

    public LinkCheckerReport setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public LinkCheckerReport setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public LinkCheckerReport setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public boolean isUrlAssessmentOk() {
        return urlAssessmentOk;
    }

    public LinkCheckerReport setUrlAssessmentOk(boolean urlAssessmentOk) {
        this.urlAssessmentOk = urlAssessmentOk;
        return this;
    }
}
