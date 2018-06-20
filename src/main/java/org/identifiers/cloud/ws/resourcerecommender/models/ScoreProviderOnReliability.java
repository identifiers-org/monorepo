package org.identifiers.cloud.ws.resourcerecommender.models;

import org.identifiers.cloud.libapi.models.linkchecker.responses.ServiceResponseScoringRequest;
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 10:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This score provider is based on reliability scoring information provided by the link checker service.
 */
public class ScoreProviderOnReliability implements ScoreProvider {
    private static final Logger logger = LoggerFactory.getLogger(ScoreProviderOnReliability.class);
    private String linkCheckerServiceHost;
    private String linkCheckerServicePort;

    public ScoreProviderOnReliability(String linkCheckerServiceHost, String linkCheckerServicePort) {
        this.linkCheckerServiceHost = linkCheckerServiceHost;
        this.linkCheckerServicePort = linkCheckerServicePort;
    }

    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        ServiceResponseScoringRequest response = ApiServicesFactory
                .getLinkCheckerService(linkCheckerServiceHost, linkCheckerServicePort)
                .getScoreForResolvedId(resolvedResource.getId(), resolvedResource.getAccessURL());
        if (response.getHttpStatus() != HttpStatus.OK) {
            // Just report the error an keep going with the default scoring
            logger.error("FAILED Reliability score for " +
                            "resource ID '{}', " +
                            "URL '{}', " +
                            "reason '{}'",
                    resolvedResource.getId(),
                    resolvedResource.getAccessURL(),
                    response.getErrorMessage());
        }
        logger.info("Reliability score for " +
                        "resource ID '{}', " +
                        "URL '{}', " +
                        "score '{}'",
                resolvedResource.getId(),
                resolvedResource.getAccessURL(),
                response.getPayload().getScore());
        return response.getPayload().getScore();
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        // TODO - Right now this is not going to be used, and there may not be anough information in a resolved resource
        // TODO - for addressing providers
        int defaultScoring = ((MAX_SCORE + MIN_SCORE) / 2);
        logger.warn("NOT IMPLEMENTED, reliability scoring for provider based on resolved " +
                "resource ID '{}' " +
                "and URL '{}' " +
                "as there may not be enough information to perform the request to the link checker on the provider. " +
                        "Default scoring of '{}' is being used",
                resolvedResource.getId(),
                resolvedResource.getAccessURL(),
                defaultScoring);
        return defaultScoring;
    }
}
