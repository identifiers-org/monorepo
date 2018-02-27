package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        List<RecommendedResource> recommendations = resolvedResources.parallelStream().map(resolvedResource -> {
            RecommendedResource recommendedResource = new RecommendedResource()
                    .setEndPointUrl(resolvedResource.getEndPointUrl())
                    .setId(resolvedResource.getId());
            if (resolvedResource.isOfficial()) {
                return recommendedResource
                        .setRecommendationIndex(99)
                        .setRecommendationExplanation("Official resource in this context");
            }
            return recommendedResource
                    .setRecommendationIndex(0)
                    .setRecommendationExplanation("This result is not official within this context");
        }).collect(Collectors.toList());
        if (recommendations.size() == 1) {
            recommendations.get(0)
                    .setRecommendationIndex(99)
                    .setRecommendationExplanation("This is the ONLY resource available within this context");
        }
        return recommendations;
    }
}
