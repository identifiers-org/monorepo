package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data model for a Resource (Provider) in the registry.
 */
@Document
public class Resource {
    @Id private BigInteger id;
    @Indexed(unique = true)
    private String mirId;
    private String accessUrl;
    private String info;
    @Indexed
    private boolean official;
    // TODO This should be a provider code
    @Indexed
    private String resourcePrefix;
    // TODO This should be Sample ID
    private String localId;
    // TODO This should be Resource Home URL
    private String resourceUrl;
    @DBRef private Institution institution;
    @DBRef private Location location;
    private BigInteger namespaceFk;
    @Transient private Namespace namespace;

    public BigInteger getId() {
        return id;
    }

    public Resource setId(BigInteger id) {
        this.id = id;
        return this;
    }

    public String getMirId() {
        return mirId;
    }

    public Resource setMirId(String mirId) {
        this.mirId = mirId;
        return this;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public Resource setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Resource setInfo(String info) {
        this.info = info;
        return this;
    }

    public boolean isOfficial() {
        return official;
    }

    public Resource setOfficial(boolean official) {
        this.official = official;
        return this;
    }

    public String getResourcePrefix() {
        return resourcePrefix;
    }

    public Resource setResourcePrefix(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
        return this;
    }

    public String getLocalId() {
        return localId;
    }

    public Resource setLocalId(String localId) {
        this.localId = localId;
        return this;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public Resource setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
        return this;
    }

    public Institution getInstitution() {
        return institution;
    }

    public Resource setInstitution(Institution institution) {
        this.institution = institution;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Resource setLocation(Location location) {
        this.location = location;
        return this;
    }

    public BigInteger getNamespaceFk() {
        return namespaceFk;
    }

    public Resource setNamespaceFk(BigInteger namespaceFk) {
        this.namespaceFk = namespaceFk;
        return this;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public Resource setNamespace(Namespace namespace) {
        this.namespace = namespace;
        return this;
    }
}
