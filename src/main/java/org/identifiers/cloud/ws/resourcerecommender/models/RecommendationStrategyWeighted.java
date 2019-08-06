package org.identifiers.cloud.ws.resourcerecommender.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;
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
@Slf4j
public class RecommendationStrategyWeighted implements RecommendationStrategy {
    @Autowired
    private ScoringFunctionProvider scoringFunctionProvider;

    // Helper method to compute a resource recommendation score for a given resolved resource
    private int getResourceRecommendationScore(ResolvedResource resolvedResource) {
        int score = 0;
        if (!resolvedResource.isDeprecatedResource() && !resolvedResource.isDeprecatedNamespace()) {
            score = scoringFunctionProvider.getFunctionComponents().stream()
                    .mapToInt(weightedScore ->
                            weightedScore.getWeight()
                                    * weightedScore.getScoreProvider().getScoreForResource(resolvedResource))
                    .sum() / 100;
        }
        log.info(String.format("Computed recommendation score within namespace '%s', for resolved resource ID '%s', MIR ID '%s', URL '%s' is '%d'",
                resolvedResource.getNamespacePrefix(),
                resolvedResource.getId(),
                resolvedResource.getMirId(),
                resolvedResource.getCompactIdentifierResolvedUrl(),
                score));
        return score;
    }

    @Override
    public List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources) {
        return resolvedResources.parallelStream().map(resolvedResource -> new ResourceRecommendation()
                        .setCompactIdentifierResolvedUrl(resolvedResource.getCompactIdentifierResolvedUrl())
                        .setId(resolvedResource.getId())
                        .setRecommendationExplanation("Function based recommendation")
                        .setRecommendationIndex(getResourceRecommendationScore(resolvedResource)))
                .collect(Collectors.toList());
    }
}
