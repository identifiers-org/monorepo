package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-10-10 14:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This document models an Institution than potentially owns resources (providers) in the registry.
 */
@Document
public class Institution {
    @Id private BigInteger id;
    @Indexed private String name;
    private String description;
    private List<BigInteger> resourcesFk;
    @Transient private List<Resource> resources;

    public BigInteger getId() {
        return id;
    }

    public Institution setId(BigInteger id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Institution setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Institution setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<BigInteger> getResourcesFk() {
        return resourcesFk;
    }

    public Institution setResourcesFk(List<BigInteger> resourcesFk) {
        this.resourcesFk = resourcesFk;
        return this;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Institution setResources(List<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Institution addResources(List<Resource> resources) {
        this.resources.addAll(resources);
        return this;
    }
}
