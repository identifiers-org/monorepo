package org.identifiers.cloud.ws.resourcerecommender.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
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
@Qualifier("recommendationStrategyWeighted")
@Primary
@Slf4j
public class RecommendationStrategyWeighted implements RecommendationStrategy {
    private final ScoringFunctionProvider scoringFunctionProvider;

    public RecommendationStrategyWeighted(ScoringFunctionProvider scoringFunctionProvider) {
        this.scoringFunctionProvider = scoringFunctionProvider;
    }

    // Helper method to compute a resource recommendation score for a given resolved resource
    private int getResourceRecommendationScore(ResolvedResource resolvedResource) {
        int score = RECOMMENDATION_SCORE_MIN;
        if (!resolvedResource.isDeprecatedResource() && !resolvedResource.isDeprecatedNamespace()) {
            score = scoringFunctionProvider.getFunctionComponents().stream()
                    .mapToInt(weightedScore ->
                            weightedScore.getWeight()
                                    * weightedScore.getScoreProvider().getScoreForResource(resolvedResource))
                    .sum() / 100;
        }
        log.info("Computed recommendation score within namespace '{}', for resolved resource ID '{}', MIR ID '{}', URL '{}'{} is '{}'",
                resolvedResource.getNamespacePrefix(),
                resolvedResource.getId(),
                resolvedResource.getMirId(),
                resolvedResource.getCompactIdentifierResolvedUrl(),
                resolvedResource.isProtectedUrls() ? "(Protected)" : "",
                score);
        return score;
    }

    @Override
    public List<ResourceRecommendation> getRecommendations(List<ResolvedResource> resolvedResources) {
        Function<ResolvedResource, Integer> scorer;
        if (resolvedResources.size() > 1) {
            scorer = this::getResourceRecommendationScore;
        } else {
            scorer = r -> 100; // Simply score maximum when only one resource is available
        }

        return resolvedResources.parallelStream().map(resolvedResource -> new ResourceRecommendation()
                        .setCompactIdentifierResolvedUrl(resolvedResource.getCompactIdentifierResolvedUrl())
                        .setId(resolvedResource.getId())
                        .setRecommendationExplanation("Function based recommendation")
                        .setRecommendationIndex(scorer.apply(resolvedResource)))
                .collect(Collectors.toList());
    }
}
