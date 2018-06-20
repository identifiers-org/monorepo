package org.identifiers.cloud.ws.resourcerecommender.models;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 10:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ScoreProviderOnReliability implements ScoreProvider {
    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        return 0;
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        return 0;
    }
}
