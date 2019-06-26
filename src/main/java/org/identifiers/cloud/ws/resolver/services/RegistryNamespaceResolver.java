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
    @Value("${org.identifiers.cloud.ws.resolver.registry.namespace.base.url.namespace.placeholder}")
    private String registryUrlPlaceholder;
    @Value("${org.identifiers.cloud.ws.resolver.registry.namespace.base.url.namespace.placeholder}")
    private String registryUrlPattern;

    @Override
    public ResolutionServiceResult resolve(ParsedCompactIdentifier parsedCompactIdentifier) {
        ResolutionServiceResult result = new ResolutionServiceResult();
        if ((parsedCompactIdentifier.getNamespace() != null)
                && (parsedCompactIdentifier.getLocalId() == null)
                && (parsedCompactIdentifier.getProviderCode() == null)) {
            // Resolve just the namespace
            // TODO Refactor out all this strings, maybe into configuration
            Location ukLocation = new Location().setCountryCode("GB").setCountryName("Great Britain");
            result.getResolvedResources()
                    .add(
                            new ResolvedResource()
                                    .setProviderCode("ebi")
                                    .setDescription("Namespace resolution to identifiers.org Central Registry")
                                    .setInstitution(
                                            new Institution()
                                                    .setHomeUrl("https://www.ebi.ac.uk/")
                                                    .setDescription("Identifiers.org Central Registry")
                                                    .setLocation(ukLocation)
                                                    .setName("EMBL-EBI"))
                                    .setLocation(ukLocation)
                                    .setOfficial(true)
                                    .setResourceHomeUrl("https://registry.identifiers.org")
                                    .setRecommendation(new Recommendation().setRecommendationIndex(100).setRecommendationExplanation("Namespace resolution to identifiers.org Central Registry"))
                                    .setCompactIdentifierResolvedUrl(registryUrlPattern.replaceFirst(registryUrlPlaceholder, parsedCompactIdentifier.getNamespace())));
            result.setResolved(true);
            log.info(String.format("Resolution request '%s' DID RESOLVED as a namespace request", parsedCompactIdentifier.getRawRequest()));
        } else {
            String errorMessage = String.format("Resolution request '%s' is NOT ABOUT A NAMESPACE", parsedCompactIdentifier.getRawRequest());
            log.error(errorMessage);
            result.setErrorMessage(errorMessage)
        }
        return result;
    }
}
