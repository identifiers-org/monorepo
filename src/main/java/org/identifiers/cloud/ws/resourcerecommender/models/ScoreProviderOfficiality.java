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

    /**
     * Get the score for a resource within the given context. The score is calculated based on whether the resource is
     * tagged as official or not, thus, getting the corresponding maximum or minimum score respectively
     * @param resolvedResource the context within which the resource will be evaluated
     * @return a scoring metric
     */
    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        if (resolvedResource.isOfficial()) {
            return MAX_SCORE;
        }
        return MIN_SCORE;
    }

    /**
     * Get the score for a provider within the given context. The score calculation is delegated to the same process run
     * for a resource
     * @param resolvedResource the context within which the provider will be evaluated
     * @return a scoring metric
     */
    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        // TODO - We can probably do better over here in the future
        return getScoreForResource(resolvedResource);
    }
}
