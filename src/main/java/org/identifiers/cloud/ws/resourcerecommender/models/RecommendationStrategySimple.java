package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:57
 * ---
 */
@Component
public class RecommendationStrategySimple implements RecommendationStrategy {
    @Override
    public List<RecommendedResource> getRecommendations(List<ResolvedResource> resolvedResources) {
        return null;
    }
}
