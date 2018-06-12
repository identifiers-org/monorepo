package org.identifiers.cloud.ws.linkchecker.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-06-12 13:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This models a resource being tracked by the service. A resource provides information on a given Compact ID, and the
 * URL is a resolved URL given that Compact ID.
 */
@RedisHash("LinkCheckerTrackedResource")
public class TrackedResource implements Serializable {

    // Resource ID within the context of a namespace / prefix
    @Id
    private String id;
    // Resolved URL
    @Indexed
    private String url;
    // When the tracking was queued / added to the link checker (UTC)
    @Indexed
    private Timestamp created;

    public String getId() {
        return id;
    }

    public TrackedResource setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TrackedResource setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public TrackedResource setCreated(Timestamp created) {
        this.created = created;
        return this;
    }
}
