package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.libapi.models.resourcerecommender.ResourceRecommendation;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
 * <p>
 * Helper for working with resolution data
 */
@Component
@Scope("prototype")
public class ResolverDataHelper {
    private static final Logger logger = LoggerFactory.getLogger(ResolverDataHelper.class);

    // Services
    @Autowired
    private ResourceRecommenderStrategy resourceRecommender;
    @Autowired
    private ResolverDataFetcher resolverDataFetcher;

    // Helpers

    /**
     * Given a URL with the placeholder "{$id}", and a local ID, it will returned the resolved form of the URL
     * @param url the URL pattern
     * @param localId the ID to embed in the URL
     * @return the resolved URL
     */
    public static String resolveUrlForLocalId(String url, String localId) {
        return url.replace("{$id}", localId);
    }

    /**
     * This helper will translate from Resource data model to Resolved Resource data model, with two missing pieces:
     * Recommendation scoring and Resolved URL, so that needs to be added later by the calling client
     * @param resource the source Resource data model
     * @return the translated ResolvedResource data model
     */
    public static ResolvedResource getResolvedResourceFrom(Resource resource) {
        return new ResolvedResource()
                .setId(Long.toString(resource.getId()))
                .setProviderCode(resource.getProviderCode())
                .setDescription(resource.getDescription()).setInstitution(resource.getInstitution())
                .setLocation(resource.getLocation()).setOfficial(resource.isOfficial())
                .setResourceHomeUrl(resource.getResourceHomeUrl());
    }

    // TODO - Maybe, refactor out the logic for resolving a resource given an ID, i.e. the URL substring substitution

    // This code may be refactored out later on
    public List<ResolvedResource> resolveResourcesForCompactId(CompactId compactId,
                                                               List<ResourceEntry> resourceEntries) {
        // Resolve the URLs
        List<ResolvedResource> resolvedResources = resourceEntries
                .parallelStream()
                .map(resourceEntry -> {
                    ResolvedResource resolvedResource = new ResolvedResource();
                    resolvedResource.setId(resourceEntry.getId());
                    resolvedResource.setResourcePrefix(resourceEntry.getResourcePrefix());
                    resolvedResource.setInfo(resourceEntry.getInfo());
                    resolvedResource.setInstitution(resourceEntry.getInstitution());
                    resolvedResource.setLocation(resourceEntry.getLocation());
                    resolvedResource.setAccessUrl(resourceEntry
                            .getAccessURL().replace("{$id}", compactId.getId()));
                    resolvedResource.setOfficial(resourceEntry.isOfficial());
                    resolvedResource.setResourceURL(resourceEntry.getResourceURL());
                    // Embed Recommendation
                    Recommendation recommendation = new Recommendation();
                    resolvedResource.setRecommendation(recommendation);
                    return resolvedResource;
                }).collect(Collectors.toList());
        // Get their recommendation index
        Map<String, ResourceRecommendation> recommendationById = getRecommendationsByResourceId(resolvedResources);
        resolvedResources.parallelStream().forEach(resolvedResource -> {
            if (recommendationById.containsKey(resolvedResource.getId())) {
                resolvedResource.getRecommendation()
                        .setRecommendationExplanation(recommendationById.get(resolvedResource.getId()).getRecommendationExplanation())
                        .setRecommendationIndex(recommendationById.get(resolvedResource.getId()).getRecommendationIndex());
            }

        });
        return resolvedResources;
    }

    public Map<String, ResourceRecommendation> getRecommendationsByResourceId(List<ResolvedResource> resourceEntries) {
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
        List<ResolvedResource> resolvedResources = new ArrayList<>();
        // TODO Review this in the future to find out whether it's the best way of doing this or not
        resolverDataFetcher.findAllNamespaces().forEach(namespace -> resolvedResources.addAll(namespace
                .getResources().parallelStream().map(resource -> getResolvedResourceFrom(resource)
                        .setCompactIdentifierResolvedUrl(resolveUrlForLocalId(resource.getUrlPattern(), resource.getSampleId()))
                        .setRecommendation(new Recommendation())).collect(Collectors.toList())));
        return resolvedResources;
    }

    // TODO - This could potentially be removed in the future?
    public List<ResolvedResource> getAllResolvedResourcesHomes() {
        return resolveAllResourcesWithTheirSampleId();
    }
}
