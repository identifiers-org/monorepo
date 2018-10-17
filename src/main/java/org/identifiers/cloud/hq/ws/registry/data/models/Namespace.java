package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a Prefix (or namespace) in the registry
 */
@Document
public class Namespace {
    @Id private BigInteger id;

    @NotNull(message = "The prefix itself must be provided, otherwise the entry makes no sense")
    @Indexed(unique = true)
    private String prefix;
    @Indexed(unique = true)
    private String mirId;
    @Indexed
    private String name;
    private String pattern;
    private String description;
    private Timestamp created;
    private Timestamp modified;
    private boolean deprecated = false;
    private Timestamp deprecationDate;
    @DBRef private List<Resource> resources;
    @DBRef private List<NamespaceSynonym> namespaceSynonyms;
    
    public List<NamespaceSynonym> getNamespaceSynonyms() {
        return namespaceSynonyms;
    }

    public Namespace setNamespaceSynonyms(List<NamespaceSynonym> namespaceSynonyms) {
        this.namespaceSynonyms = namespaceSynonyms;
        return this;
    }

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
