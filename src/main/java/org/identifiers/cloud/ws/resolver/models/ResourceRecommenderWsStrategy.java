package org.identifiers.cloud.ws.resolver.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.models.resourcerecommender.Location;
import org.identifiers.cloud.libapi.models.resourcerecommender.ResolvedResource;
import org.identifiers.cloud.libapi.models.resourcerecommender.ResourceRecommendation;
import org.identifiers.cloud.libapi.services.ResourceRecommenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-02-27 21:34
 * ---
 */
@Component
@Scope("prototype")
@Slf4j
public class ResourceRecommenderWsStrategy implements ResourceRecommenderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ResourceRecommenderWsStrategy.class);

    @Autowired
    ResourceRecommenderService resourceRecommenderService;

    @Override
    public List<ResourceRecommendation> getRecommendations(List<org.identifiers.cloud.ws.resolver.models.ResolvedResource> resources) throws ResourceRecommenderStrategyException {
        // Whatever happens, the client library will always return a default empty answer that is valid
        if (resources.isEmpty()) {
            return Collections.emptyList();
        } else if (resources.size() == 1) {
            return resources.stream().map(this::getResolverLibapiResolverResource).map(resolvedResource ->
                    new ResourceRecommendation()
                            .setRecommendationExplanation("Only resource available for this CID")
                            .setRecommendationIndex(100)
                            .setId(resolvedResource.getId())
            ).collect(Collectors.toList());
        } else {
            return resourceRecommenderService
                    .requestRecommendations(resources.parallelStream()
                            .map(this::getResolverLibapiResolverResource)
                            .collect(Collectors.toList()))
                    .getPayload()
                    .getResourceRecommendations();
        }
    }

    ResolvedResource getResolverLibapiResolverResource(org.identifiers.cloud.ws.resolver.models.ResolvedResource resolvedResource) {
        return new ResolvedResource()
                .setOfficial(resolvedResource.isOfficial())
                .setCompactIdentifierResolvedUrl(resolvedResource.getCompactIdentifierResolvedUrl())
                .setId(Long.toString(resolvedResource.getId()))
                .setResourceHomeUrl(resolvedResource.getResourceHomeUrl())
                .setDeprecatedNamespace(resolvedResource.isDeprecatedNamespace())
                .setNamespaceDeprecationDate(resolvedResource.getNamespaceDeprecationDate())
                .setDeprecatedResource(resolvedResource.isDeprecatedResource())
                .setResourceDeprecationDate(resolvedResource.getResourceDeprecationDate())
                .setMirId(resolvedResource.getMirId())
                .setNamespacePrefix(resolvedResource.getNamespacePrefix())
                .setProtectedUrls(resolvedResource.isProtectedUrls())
                .setAuthHelpUrl(resolvedResource.getAuthHelpUrl())
                .setAuthHelpDescription(resolvedResource.getAuthHelpDescription())
                .setRenderProtectedLanding(resolvedResource.isRenderProtectedLanding())
                .setLocation(new Location()
                        .setCountryName(resolvedResource.getLocation().getCountryName())
                        .setCountryCode(resolvedResource.getLocation().getCountryCode()));
    }
}
