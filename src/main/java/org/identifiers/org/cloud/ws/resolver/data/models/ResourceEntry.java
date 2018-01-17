package org.identifiers.org.cloud.ws.resolver.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 16:15
 * ---
 */
@RedisHash("resourceEntries")
public class ResourceEntry implements Serializable {
    @Id
    private String id;
    private String accessUrl;
    private String info;
    private String institution;
    private String location;
    private boolean official;
    @Indexed
    private String resourcePrefix;
    private String localId;
    private String testString;

    public ResourceEntry(String id, String accessUrl, String info, String institution, String location, boolean official, String resourcePrefix, String localId, String testString) {
        this.id = id;
        this.accessUrl = accessUrl;
        this.info = info;
        this.institution = institution;
        this.location = location;
        this.official = official;
        this.resourcePrefix = resourcePrefix;
        this.localId = localId;
        this.testString = testString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public String getResourcePrefix() {
        return resourcePrefix;
    }

    public void setResourcePrefix(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
