package org.identifiers.cloud.ws.resolver.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resolver.data.models.Institution;
import org.identifiers.cloud.ws.resolver.data.models.Location;
import org.identifiers.cloud.ws.resolver.models.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.resolver.models.Recommendation;
import org.identifiers.cloud.ws.resolver.models.ResolvedResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.services
 * Timestamp: 2019-06-26 15:05
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This resolver specialises in resolution of namespace requests to their corresponding entry in identifiers.org central
 * registry services.
 */
@Component
@Qualifier("RegistryNamespaceResolver")
@Slf4j
public class RegistryNamespaceResolver implements ResolutionService {
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.registry.namespace.base.url.placeholder}")
    private String registryUrlPlaceholder;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.registry.namespace.base.url}")
    private String registryUrlPattern;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.providercode}")
    private String providerCode;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.resource.home.url}")
    private String resourceHomeUrl;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.resource.description}")
    private String resourceDescription;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.location.country.code}")
    private String locationCountryCode;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.location.country.name}")
    private String locationCountryName;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.institution.home.url}")
    private String institutionHomeUrl;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.institution.description}")
    private String institutionDescription;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.institution.name}")
    private String institutionName;
    @Value("${org.identifiers.cloud.ws.resolver.namespaceresolver.recommendation.explanation}")
    private String recommendationExplanation;



    @Override
    public ResolutionServiceResult resolve(ParsedCompactIdentifier parsedCompactIdentifier) {
        ResolutionServiceResult result = new ResolutionServiceResult();
        if ((parsedCompactIdentifier.getNamespace() != null)
                && (parsedCompactIdentifier.getLocalId() == null)
                && (parsedCompactIdentifier.getProviderCode() == null)) {
            // Resolve just the namespace
            // TODO Refactor out all this strings, maybe into configuration
            Location ukLocation = new Location().setCountryCode(locationCountryCode).setCountryName(locationCountryName);
            result.getResolvedResources()
                    .add(
                            new ResolvedResource()
                                    .setProviderCode(providerCode)
                                    .setDescription(resourceDescription)
                                    .setInstitution(
                                            new Institution()
                                                    .setHomeUrl(institutionHomeUrl)
                                                    .setDescription(institutionDescription)
                                                    .setLocation(ukLocation)
                                                    .setName(institutionName))
                                    .setLocation(ukLocation)
                                    .setOfficial(true)
                                    .setResourceHomeUrl(resourceHomeUrl)
                                    .setRecommendation(new Recommendation().setRecommendationIndex(100).setRecommendationExplanation(recommendationExplanation))
                                    .setCompactIdentifierResolvedUrl(registryUrlPattern.replace(registryUrlPlaceholder, parsedCompactIdentifier.getNamespace())));
            result.setResolved(true);
            log.info(String.format("Resolution request '%s' DID RESOLVED as a namespace request", parsedCompactIdentifier.getRawRequest()));
        } else {
            String errorMessage = String.format("Resolution request '%s' is NOT ABOUT A NAMESPACE", parsedCompactIdentifier.getRawRequest());
            log.error(errorMessage);
            result.setErrorMessage(errorMessage);
        }
        return result;
    }
}
