package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.libapi.models.ResourceRecommender.ResourceRecommendation;
import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.daemons.models
 * Timestamp: 2018-02-27 21:26
 * ---
 */
public interface ResourceRecommenderStrategy {
    List<ResourceRecommendation> getRecommendations(List<ResourceEntry> resources) throws ResourceRecommenderStrategyException;
}
