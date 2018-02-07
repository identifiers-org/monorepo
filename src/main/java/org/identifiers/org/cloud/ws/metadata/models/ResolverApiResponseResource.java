package org.identifiers.org.cloud.ws.metadata.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 11:37
 * ---
 *
 * This class represents a resolved resource, part of the response from the Resolver API
 */
// TODO - REFACTOR OUT THIS ENTITY TO BE REUSED BY CLIENTS AS WELL
    @JsonIgnoreProperties(ignoreUnknown = true)
public class ResolverApiResponseResource implements Serializable {

    private String accessUrl;
    private String info;
    private String institution;
    private String location;
    private boolean official;

    public String getAccessUrl() {
        return accessUrl;
    }

    public ResolverApiResponseResource setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ResolverApiResponseResource setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getInstitution() {
        return institution;
    }

    public ResolverApiResponseResource setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ResolverApiResponseResource setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isOfficial() {
        return official;
    }

    public ResolverApiResponseResource setOfficial(boolean official) {
        this.official = official;
        return this;
    }
}
