package org.identifiers.cloud.ws.resourcerecommender.models;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 9:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Score Provider calculates its score taking into account whether a resource is tagged as official or not.
 */
public class ScoreProviderOfficiality implements ScoreProvider {
    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        if (resolvedResource.isOfficial()) {
            return MAX_SCORE;
        }
        return MIN_SCORE;
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        // TODO - We can probably do better over here in the future
        return getScoreForResource(resolvedResource);
    }
}
