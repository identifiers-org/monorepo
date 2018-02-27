package org.identifiers.cloud.ws.resourcerecommender.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
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
    private static Logger logger = LoggerFactory.getLogger(ResourceRecommenderApiModel.class);
    private static String runningSessionId = UUID.randomUUID().toString();

    @Autowired
    private RecommendationStrategy recommendationStrategy;

    private List<RecommendedResource> evaluateRecommendations(List<ResolvedResource> resolvedResources) {
        return recommendationStrategy.getRecommendations(resolvedResources);
    }

    public ResourceRecommenderApiResponse getRecommendations(ResourceRecommenderRequest request) {
        // NOTE - I know, I should not use try-catch as an if-else block, but in this case, this logic is sooo simple...
        try {
            return new ResourceRecommenderApiResponse()
                    .setPayload(evaluateRecommendations(request.getResolvedResources()));
        } catch (RuntimeException e) {
            logger.error("The following ERROR occurred while trying to evaluate resolved resources recommendations: " +
                    "'{}'", e.getMessage());
            return new ResourceRecommenderApiResponse()
                    .setErrorMessage("An error occurred while trying to evaluate the recommendations " +
                            "for the given resolved resources")
                    .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String livenessCheck() {
        return runningSessionId;
    }

    public String readinessCheck() {
        return runningSessionId;
    }
}
