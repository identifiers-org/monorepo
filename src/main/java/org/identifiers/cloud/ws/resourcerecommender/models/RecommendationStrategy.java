package org.identifiers.cloud.ws.resourcerecommender.models;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:56
 * ---
 */
public interface RecommendationStrategy {
    List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources);
}
