package org.identifiers.cloud.ws.linkchecker.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

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
@RedisHash("linkCheckerProviderEntry")
public class ProviderEntry implements Serializable {
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
    private List<CheckedUrl> history;

    public String getId() {
        return id;
    }

    public ProviderEntry setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ProviderEntry setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProviderEntry setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getInstitution() {
        return institution;
    }

    public ProviderEntry setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ProviderEntry setLocation(String location) {
        this.location = location;
        return this;
    }

    public List<CheckedUrl> getHistory() {
        return history;
    }

    public ProviderEntry setHistory(List<CheckedUrl> history) {
        this.history = history;
        return this;
    }
}
