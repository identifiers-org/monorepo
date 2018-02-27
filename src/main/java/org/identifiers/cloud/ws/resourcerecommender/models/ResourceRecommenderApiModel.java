package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:04
 * ---
 */
@Component
@Scope("prototype")
public class ResourceRecommenderApiModel {
    private static String runningSessionId = UUID.randomUUID().toString();

    @Autowired
    private RecommendationStrategy recommendationStrategy;

    private List<RecommendedResource> evaluateRecommendations(List<ResolvedResource> resolvedResources) {
        recommendationStrategy.getRecommendations(resolvedResources);
    }

    public String livenessCheck() {
        return runningSessionId;
    }

    public String readinessCheck() {
        return runningSessionId;
    }
}
