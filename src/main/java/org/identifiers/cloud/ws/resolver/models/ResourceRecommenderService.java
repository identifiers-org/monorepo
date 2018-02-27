package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-02-27 21:34
 * ---
 */
@Component
@Scope("prototype")
public class ResourceRecommenderService implements ResourceRecommenderStrategy {
    @Override
    public List<RecommendedResource> getRecommendations(List<ResourceEntry> resources) throws ResourceRecommenderStrategyException{
        return null;
    }
}
