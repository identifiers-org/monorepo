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
    protected Timestamp timestamp;
    

    public String getUrl() {
        return url;
    }

    public LinkCheckRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public LinkCheckRequest setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public int compareTo(LinkCheckRequest o) {
        return this.timestamp.compareTo(o.getTimestamp());
    }

}
