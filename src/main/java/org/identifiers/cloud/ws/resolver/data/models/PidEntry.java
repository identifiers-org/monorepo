package org.identifiers.cloud.ws.resolver.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 15:20
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@RedisHash("pidentries")
public class PidEntry implements Serializable {
    @Id
    private String id;
    private String name;
    private String pattern;
    private String definition;
    @Indexed
    private String prefix;
    private String url;
    private String[] synonyms;
    private boolean prefixed;
    private @Reference ResourceEntry[] resources;

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

    public ResourceEntry[] getResources() {
        return resources;
    }

    public void setResources(ResourceEntry[] resources) {
        this.resources = resources;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }
}
