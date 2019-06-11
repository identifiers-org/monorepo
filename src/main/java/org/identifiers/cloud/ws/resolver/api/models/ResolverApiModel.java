package org.identifiers.cloud.ws.resolver.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resolver.api.ApiCentral;
import org.identifiers.cloud.ws.resolver.api.responses.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponseResolve;
import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 10:31
 * ---
 */
// Well, when using Spring Boot, you have to fit in a particular way of doing things. Is it the right way?
@Component
@Scope("prototype")
@Slf4j
public class ResolverApiModel {
    // External Helpers
    @Autowired
    private ResolverDataFetcher resolverDataFetcher;
    @Autowired
    private ResolverDataHelper resolverDataHelper;
    @Autowired
    private CompactIdParsingHelper compactIdParsingHelper;

    // Helpers
    private ServiceResponseResolve createDefaultResponse() {
        return (ServiceResponseResolve)
                new ServiceResponseResolve()
                .setApiVersion(ApiCentral.apiVersion)
                .setPayload(new ResponseResolvePayload().setResolvedResources(new ArrayList<>()));
    }

    private CompactId getCompactIdentifier(String compactId, ServiceResponseResolve response) {
        try {
            return new CompactId(compactId);
        } catch (CompactIdException e) {
            response.setErrorMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private void verifyCompactIdentifier(String namespace, String localId, ServiceResponseResolve response) {
        // TODO
        Namespace registryNamespace = resolverDataFetcher.findNamespaceByPrefix(namespace);
        if (registryNamespace == null) {
            String errorMessage = String.format("UNKNOWN namespace '%s' when verifying local ID '%s'", namespace,
                    localId);
            log.error(errorMessage);
            response.setErrorMessage(errorMessage);
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else {
            // Verify regular expression
            if (!localId.matches(registryNamespace.getPattern())) {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                String errorMessage = String.format("For namespace '%s', provided local ID '%s' DOES NOT MATCH local IDs definition pattern '%s'", namespace, localId, registryNamespace.getPattern());
                log.error(errorMessage);
                response.setErrorMessage(errorMessage);
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
        }
    }

    // END - Helpers

    // --- Resolution API ---

    public ServiceResponseResolve resolveRawCompactId(String rawCompactId) {
        // This is the only entry method right now
        ServiceResponseResolve response = createDefaultResponse();
        ParsedCompactIdentifier parsedCompactIdentifier = compactIdParsingHelper.parseCompactIdRequest(rawCompactId);
        if ((parsedCompactIdentifier.getLocalId() != null && (parsedCompactIdentifier.getNamespace() != null))) {
            // Verify compact identifier
            verifyCompactIdentifier(parsedCompactIdentifier.getNamespace(), parsedCompactIdentifier.getLocalId(), response);
            if (!response.getHttpStatus().is2xxSuccessful()) {
                return response;
            }
            // Get the resources
            List<Resource> resources = resolverDataFetcher.findResourcesByPrefix(parsedCompactIdentifier.getNamespace());
            log.info(String.format("CompactId '%s', with prefix '%s' got #%d resources back from the data backend",
                    parsedCompactIdentifier.getRawRequest(), parsedCompactIdentifier.getNamespace(), resources.size()));
            // Have we found any resources?
            if (resources.isEmpty()) {
                // If no providers, produce error response
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setErrorMessage(String.format("No providers found for Compact ID '%s'", parsedCompactIdentifier.getRawRequest()));
                return response;
            }
            // Check for provider
            if (parsedCompactIdentifier.getProviderCode() != null) {
                // Look for the one with the given provider code
                List<Resource> filteredResources = resources.stream()
                        .filter(resource -> {
                            if (resource.getProviderCode() != null) {
                                return resource.getProviderCode().equals(parsedCompactIdentifier.getProviderCode());
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
                log.info(String.format("CompactId '%s', with prefix '%s' got #%d resources with provider code '%s'",
                        parsedCompactIdentifier.getRawRequest(),
                        parsedCompactIdentifier.getNamespace(),
                        filteredResources.size(),
                        parsedCompactIdentifier.getProviderCode()));
                if (filteredResources.isEmpty()) {
                    String errorMessage = String.format("For Compact ID '%s', prefix '%s', " +
                            "NO RESOURCES FOUND with PROVIDER CODE '%s'",
                            parsedCompactIdentifier.getRawRequest(),
                            parsedCompactIdentifier.getNamespace(),
                            parsedCompactIdentifier.getProviderCode());
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setErrorMessage(errorMessage);
                    log.error(errorMessage);
                    return response;
                }
                // Resolve the selected resources
                response.getPayload().setResolvedResources(resolverDataHelper.resolveResourcesForCompactId(parsedCompactIdentifier, filteredResources));
                return response;
            }
            // Resolve all the resources
            response.getPayload().setResolvedResources(resolverDataHelper.resolveResourcesForCompactId(parsedCompactIdentifier, resources));
            return response;
        }
        // Default response
        String errorMessage = String.format("INVALID Compact Identifier resolution request for '%s'", rawCompactId);
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
