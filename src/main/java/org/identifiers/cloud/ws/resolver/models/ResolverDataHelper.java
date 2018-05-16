package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.libapi.models.resourcerecommender.ResourceRecommendation;
import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.identifiers.cloud.ws.resolver.models.api.responses.Recommendation;
import org.identifiers.cloud.ws.resolver.models.api.responses.ResolvedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-05-16 15:39
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Helper for working with resolution data
 */
@Component
@Scope("prototype")
public class ResolverDataHelper {
    private static final Logger logger = LoggerFactory.getLogger(ResolverDataHelper.class);

    @Autowired
    private ResourceRecommenderStrategy resourceRecommender;

    // This code may be refactored out later on
    public List<ResolvedResource> resolveResourcesForCompactId(CompactId compactId,
                                                                List<ResourceEntry> resourceEntries,
                                                                Map<String, ResourceRecommendation> recommendationById) {
        return resourceEntries
                .parallelStream()
                .map(resourceEntry -> {
                    ResolvedResource resolverApiResponseResource = new ResolvedResource();
                    resolverApiResponseResource.setId(resourceEntry.getId());
                    resolverApiResponseResource.setResourcePrefix(resourceEntry.getResourcePrefix());
                    resolverApiResponseResource.setInfo(resourceEntry.getInfo());
                    resolverApiResponseResource.setInstitution(resourceEntry.getInstitution());
                    resolverApiResponseResource.setLocation(resourceEntry.getLocation());
                    resolverApiResponseResource.setAccessUrl(resourceEntry
                            .getAccessURL().replace("{$id}", compactId.getId()));
                    resolverApiResponseResource.setOfficial(resourceEntry.isOfficial());
                    // Embed Recommendation
                    Recommendation recommendation = new Recommendation();
                    if (recommendationById.containsKey(resourceEntry.getId())) {
                        recommendation
                                .setRecommendationExplanation(recommendationById.get(resourceEntry.getId()).getRecommendationExplanation())
                                .setRecommendationIndex(recommendationById.get(resourceEntry.getId()).getRecommendationIndex());
                    }
                    resolverApiResponseResource.setRecommendation(recommendation);
                    return resolverApiResponseResource;
                }).collect(Collectors.toList());
    }

    public Map<String, ResourceRecommendation> getRecommendationsByResourceId(List<ResourceEntry> resourceEntries) {
        try {
            return resourceRecommender
                    .getRecommendations(resourceEntries)
                    .parallelStream()
                    .collect(Collectors.toMap(ResourceRecommendation::getId,
                            recommendedResource -> recommendedResource,
                            (oldValue, newValue) -> oldValue));
        } catch (ResourceRecommenderStrategyException e) {
            logger.error("The following ERROR occurred while trying to get recommendations for the given resources," +
                    " ERROR '{}'", e.getMessage());
        }
        return new HashMap<>();
    }

    public List<ResolvedResource> resolveAllResourcesWithTheirSampleId() {
        // TODO
        return null;
    }
}
