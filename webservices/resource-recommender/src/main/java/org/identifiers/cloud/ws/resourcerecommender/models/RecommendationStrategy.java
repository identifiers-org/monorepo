package org.identifiers.cloud.ws.resourcerecommender.models;

import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:56
 * ---
 */
public interface RecommendationStrategy {
    int RECOMMENDATION_SCORE_MAX = 100;
    int RECOMMENDATION_SCORE_MIN = 0;
    List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources);
}
