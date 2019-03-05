package org.identifiers.cloud.hq.ws.registry.api.data.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models how the microservice exposes information about resources in the registry through its Resolution API.
 */
// TODO Refactor with lombok
public class Resource implements Serializable {
    private BigInteger id;
    private String mirId;
    private String accessUrl;
    private String info;
    private boolean official;
    // TODO This should be a provider code
    private String resourcePrefix;
    // TODO This should be Sample ID
    private String localId;
    // TODO This should be Resource Home URL
    private String resourceUrl;
    private String institution;
    private String location;

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

    public String getInstitution() {
        return institution;
    }

    public Resource setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Resource setLocation(String location) {
        this.location = location;
        return this;
    }
}
