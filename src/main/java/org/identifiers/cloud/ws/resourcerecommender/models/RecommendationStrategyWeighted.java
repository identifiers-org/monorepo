package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-19 15:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class RecommendationStrategyWeighted implements RecommendationStrategy {
    @Override
    public List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources) {
        return null;
    }
}
