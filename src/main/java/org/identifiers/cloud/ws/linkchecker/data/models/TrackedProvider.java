package org.identifiers.cloud.ws.linkchecker.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;

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
public class TrackedProvider implements Serializable {
    // Provider ID within the context of a namespace or prefix
    @Id
    private String id;
    // A description of this provider within the context of a namespace or prefix
    private String description;
    // Institution information
    private String institution;
    // Location information on this provider within the context of a namespace or prefix, if available
    @Indexed
    private String location;
    // Home URL for this provider within the context of a namespace or prefix
    @Indexed
    private String url;
    // When the tracking was queued / added to the link checker (UTC)
    @Indexed
    private Timestamp created;

    public String getId() {
        return id;
    }

    public TrackedProvider setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TrackedProvider setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getInstitution() {
        return institution;
    }

    public TrackedProvider setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public TrackedProvider setLocation(String location) {
        this.location = location;
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
        return created;
    }

    public TrackedProvider setCreated(Timestamp created) {
        this.created = created;
        return this;
    }
}
