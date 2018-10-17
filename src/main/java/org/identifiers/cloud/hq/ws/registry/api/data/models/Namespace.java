package org.identifiers.cloud.hq.ws.registry.api.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models how the microservice exposes information about namespaces in the registry through its Resolution API.
 */
public class Namespace implements Serializable {
    private BigInteger id;
    private String prefix;
    private String mirId;
    private String name;
    private String pattern;
    @JsonSerialize
    @JsonProperty("definition")
    private String description;
    private Timestamp created;
    private Timestamp modified;
    private boolean deprecated = false;
    private Timestamp deprecationDate;
    private List<Resource> resources = new ArrayList<>();

    public BigInteger getId() {
        return id;
    }

    public Namespace setId(BigInteger id) {
        this.id = id;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Namespace setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getMirId() {
        return mirId;
    }

    public Namespace setMirId(String mirId) {
        this.mirId = mirId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Namespace setName(String name) {
        this.name = name;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public Namespace setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Namespace setDescription(String description) {
        this.description = description;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Namespace setCreated(Timestamp created) {
        this.created = created;
        return this;
    }

    public Timestamp getModified() {
        return modified;
    }

    public Namespace setModified(Timestamp modified) {
        this.modified = modified;
        return this;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public Namespace setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public Timestamp getDeprecationDate() {
        return deprecationDate;
    }

    public Namespace setDeprecationDate(Timestamp deprecationDate) {
        this.deprecationDate = deprecationDate;
        return this;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Namespace setResources(List<Resource> resources) {
        this.resources = resources;
        return this;
    }
}
