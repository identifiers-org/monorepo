package org.identifiers.cloud.ws.resourcerecommender.models.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.identifiers.cloud.ws.resourcerecommender.models.ResourceRecommendation;

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
public class ResponseRecommendPayload implements Serializable {
    List<ResourceRecommendation> resourceRecommendations;

    public List<ResourceRecommendation> getResourceRecommendations() {
        return resourceRecommendations;
    }

    public ResponseRecommendPayload setResourceRecommendations(List<ResourceRecommendation> resourceRecommendations) {
        this.resourceRecommendations = resourceRecommendations;
        return this;
    }
}