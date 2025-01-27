package org.identifiers.cloud.ws.resourcerecommender.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;
import org.springframework.util.PatternMatchUtils;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 9:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Score Provider calculates its score taking into account whether a resource is hosted by EBI or not.
 */
@Slf4j
public class ScoreProviderEbi implements ScoreProvider {
    private boolean isEbi(final ResolvedResource resolvedResource) {
        return PatternMatchUtils.simpleMatch("http*://*ebi.ac.uk/*", resolvedResource.getCompactIdentifierResolvedUrl());
    }

    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        if (isEbi(resolvedResource)) {
            log.debug("ID {} is EBI", resolvedResource.getId());
            return MAX_SCORE;
        }
        log.debug("ID {} is not EBI", resolvedResource.getId());
        return MIN_SCORE;
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        return getScoreForResource(resolvedResource);
    }
}
