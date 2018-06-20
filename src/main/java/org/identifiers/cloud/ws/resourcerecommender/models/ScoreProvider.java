package org.identifiers.cloud.ws.resourcerecommender.models;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 9:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the interface of a scoring provider, which is a strategy to score a resolved resource from different point of
 * views, attending to different aspects of it.
 */
public interface ScoreProvider {
    int MAX_SCORE = 100;
    int MIN_SCORE = 0;

    int getScoreForResource(ResolvedResource resolvedResource) throws ScoreProviderException;

    int getScoreForProvider(ResolvedResource resolvedResource) throws ScoreProviderException;
}
