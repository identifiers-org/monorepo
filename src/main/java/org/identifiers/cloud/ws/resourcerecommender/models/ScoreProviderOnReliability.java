package org.identifiers.cloud.ws.resourcerecommender.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 10:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
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
        // TODO
        return 0;
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        // TODO
        return 0;
    }
}
