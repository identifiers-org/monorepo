package org.identifiers.cloud.ws.resourcerecommender.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-03-06 15:26
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRecommendPayload implements Serializable {
    private List<ResolvedResource> resolvedResources;

    public List<ResolvedResource> getResolvedResources() {
        return resolvedResources;
    }

    public RequestRecommendPayload setResolvedResources(List<ResolvedResource> resolvedResources) {
        this.resolvedResources = resolvedResources;
        return this;
    }
}
