package org.identifiers.org.cloud.ws.resolver.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 15:20
 * ---
 */
@RedisHash("pidentries")
public class PidEntry {
    @Id
    private String id;
    private String name;
    private String pattern;
    private String definition;
    @Indexed
    private String prefix;
    private String url;
    private boolean prefixed;
    private ResourceEntry[] resourceEntries;

    public PidEntry(String id, String name, String pattern, String definition, String prefix, String url, boolean prefixed, ResourceEntry[] resourceEntries) {
        this.id = id;
        this.name = name;
        this.pattern = pattern;
        this.definition = definition;
        this.prefix = prefix;
        this.url = url;
        this.prefixed = prefixed;
        this.resourceEntries = resourceEntries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPrefixed() {
        return prefixed;
    }

    public void setPrefixed(boolean prefixed) {
        this.prefixed = prefixed;
    }

    public ResourceEntry[] getResourceEntries() {
        return resourceEntries;
    }

    public void setResourceEntries(ResourceEntry[] resourceEntries) {
        this.resourceEntries = resourceEntries;
    }
}
