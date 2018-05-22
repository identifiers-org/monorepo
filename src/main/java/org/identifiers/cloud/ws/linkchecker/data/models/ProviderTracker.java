package org.identifiers.cloud.ws.linkchecker.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.collections.transformation.SortedList;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 11:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models a scoring entry, at provider level, within the context of a namespace or prefix, i.e. this entity
 * will be used for tracking the provider home URL.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@RedisHash("linkCheckerProviderTracker")
public class ProviderTracker implements Serializable {
    // Provider ID within the context of a namespace or prefix
    private String id;
    // Home URL for this provider within the context of a namespace or prefix
    private String url;
    // A description of this provider within the context of a namespace or prefix
    private String description;
    // Institution information
    private String institution;
    // Location information on this provider within the context of a namespace or prefix, if available
    private String location;
    // Historical information
    private SortedList<CheckedUrl> history;
    // When the tracking was queued / added to the link checker (UTC)
    private Timestamp created;

    public String getId() {
        return id;
    }

    public ProviderTracker setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ProviderTracker setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProviderTracker setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getInstitution() {
        return institution;
    }

    public ProviderTracker setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ProviderTracker setLocation(String location) {
        this.location = location;
        return this;
    }

    public SortedList<CheckedUrl> getHistory() {
        return history;
    }

    public ProviderTracker setHistory(SortedList<CheckedUrl> history) {
        this.history = history;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public ProviderTracker setCreated(Timestamp created) {
        this.created = created;
        return this;
    }
}
