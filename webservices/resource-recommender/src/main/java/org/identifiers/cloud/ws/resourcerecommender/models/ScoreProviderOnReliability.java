package org.identifiers.cloud.ws.resourcerecommender.models;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.libapi.services.LinkCheckerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.Duration;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 10:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This score provider is based on reliability scoring information provided by the link checker service.
 *
 * Simple caching is employed to reduce http calls to link checker
 */
public class ScoreProviderOnReliability implements ScoreProvider {
    private static final Logger logger = LoggerFactory.getLogger(ScoreProviderOnReliability.class);
    private final Cache<Long, Integer> resourceScoreCache;

    private final LinkCheckerService linkCheckerService;
    public ScoreProviderOnReliability(
            LinkCheckerService linkCheckerService,
            int maxCacheSize,
            Duration maxCachedDuration
    ) {
        this.linkCheckerService = linkCheckerService;
        resourceScoreCache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheSize)
                .expireAfterWrite(maxCachedDuration)
                .build();
    }

    @Override
    public int getScoreForResource(ResolvedResource resolvedResource) {
        Integer cachedScore = resourceScoreCache.getIfPresent(resolvedResource.getId());
        if (cachedScore != null) {
            logger.debug("Using cached reliability score for resource {}", resolvedResource.getId());
            return cachedScore;
        }

        var response = linkCheckerService.getScoreForResolvedId(String.valueOf(resolvedResource.getId()),
                                                                resolvedResource.getCompactIdentifierResolvedUrl(),
                                                                resolvedResource.isProtectedUrls());
        if (response.getHttpStatus() != HttpStatus.OK) {
            // Just report the error an keep going with the default scoring
            logger.error("FAILED Reliability score for " +
                            "resource ID '{}', " +
                            "URL '{}', " +
                            "ProtectedUrls? '{}', " +
                            "reason '{}'",
                    resolvedResource.getId(),
                    resolvedResource.getCompactIdentifierResolvedUrl(),
                    resolvedResource.isProtectedUrls(),
                    response.getErrorMessage());
        }
        logger.debug("ID {} reliability score: {}", resolvedResource.getId(), response.getPayload().getScore());
        resourceScoreCache.put(resolvedResource.getId(), response.getPayload().getScore());

        return response.getPayload().getScore();
    }

    @Override
    public int getScoreForProvider(ResolvedResource resolvedResource) {
        // TODO - Right now this is not going to be used, and there may not be enough information in a resolved resource
        // TODO - for addressing providers
        int defaultScoring = ((MAX_SCORE + MIN_SCORE) / 2);
        logger.warn("NOT IMPLEMENTED, reliability scoring for provider based on resolved " +
                "resource ID '{}' " +
                "and URL '{}' " +
                "as there may not be enough information to perform the request to the link checker on the provider. " +
                        "Default scoring of '{}' is being used",
                resolvedResource.getId(),
                resolvedResource.getCompactIdentifierResolvedUrl(),
                defaultScoring);
        return defaultScoring;
    }
}
