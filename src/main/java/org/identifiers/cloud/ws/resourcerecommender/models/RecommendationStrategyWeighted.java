package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-19 15:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Profile("recommendationStrategyWeighted")
public class RecommendationStrategyWeighted implements RecommendationStrategy {
    @Autowired
    private ScoringFunctionProvider scoringFunctionProvider;

    // Helper method to compute a resource recommendation score for a given resolved resource
    private int getResourceRecommendationScore(ResolvedResource resolvedResource) {
        return scoringFunctionProvider.getFunctionComponents().parallelStream()
                .mapToInt(weightedScore ->
                        weightedScore.getWeight()
                                * weightedScore.getScoreProvider().getScoreForResource(resolvedResource))
                .sum() / 100;
    }

    @Override
    public List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources) {
        return resolvedResources.parallelStream().map(resolvedResource -> new ResourceRecommendation()
                        .setAccessURL(resolvedResource.getAccessURL())
                        .setId(resolvedResource.getId())
                        .setRecommendationExplanation("Function based recommendation")
                        .setRecommendationIndex(getResourceRecommendationScore(resolvedResource)))
                .collect(Collectors.toList());
    }
}
