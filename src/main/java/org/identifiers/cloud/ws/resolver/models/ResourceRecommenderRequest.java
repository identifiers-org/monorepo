package org.identifiers.cloud.ws.resolver.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:18
 * ---
 */
// TODO - This will be externalized later in a separated library with models and service wrappers for all identifiers.org services
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceRecommenderRequest implements Serializable {
    private List<ResolvedResource> resolvedResources;

    public List<ResolvedResource> getResolvedResources() {
        return resolvedResources;
    }

    public ResourceRecommenderRequest setResolvedResources(List<ResolvedResource> resolvedResources) {
        this.resolvedResources = resolvedResources;
        return this;
    }
}
