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
 * Timestamp: 2018-05-26 9:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This models a provider being tracked by the service.
 */
@RedisHash("LinkCheckerTrackedProvider")
public class TrackedProvider implements Serializable {
    // Provider ID within the context of a namespace or prefix
    @Id
    private String id;
    // Home URL for this provider within the context of a namespace or prefix
    @Indexed
    private String url;
    // When the tracking was queued / added to the link checker (UTC)
    @Indexed
    private String created = (new Timestamp(new Date().getTime())).toString();

    public String getId() {
        return id;
    }

    public TrackedProvider setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TrackedProvider setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getCreated() {
        return Timestamp.valueOf(created);
    }

    public TrackedProvider setCreated(Timestamp created) {
        this.created = created.toString();
        return this;
    }
}
