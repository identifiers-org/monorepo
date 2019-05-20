package org.identifiers.cloud.ws.resolver.api.models;

import org.identifiers.cloud.ws.resolver.api.ApiCentral;
import org.identifiers.cloud.ws.resolver.api.responses.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponseResolve;
import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ResolverApiModel {
    private static Logger logger = LoggerFactory.getLogger(ResolverApiModel.class);

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
            logger.error(errorMessage);
            response.setErrorMessage(errorMessage);
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else {
            // Verify regular expression
            if (!localId.matches(registryNamespace.getPattern())) {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                String errorMessage = String.format("For namespace '%s', provided local ID '%s' DOES NOT MATCH local IDs definition pattern '%s'", namespace, localId, registryNamespace.getPattern());
                logger.error(errorMessage);
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
            // Check for provider
            if (parsedCompactIdentifier.getProviderCode() != null) {
                return resolveCompactId(CompactId.getCompactIdString(parsedCompactIdentifier.getNamespace(), parsedCompactIdentifier.getLocalId()), parsedCompactIdentifier.getProviderCode());
            }
            return resolveCompactId(CompactId.getCompactIdString(parsedCompactIdentifier.getNamespace(), parsedCompactIdentifier.getLocalId()));
        }
        String errorMessage = String.format("INVALID Compact Identifier resolution request for '%s'", rawCompactId);
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setErrorMessage(errorMessage);
        return response;
    }

    // TODO - Document this API method
    private ServiceResponseResolve resolveCompactId(String compactIdParameter) throws ResolverApiException {
        ServiceResponseResolve response = createDefaultResponse();
        CompactId compactId = getCompactIdentifier(compactIdParameter, response);
        if (compactId != null) {
            // TODO - Check if prefix is null, as we may want to perform a more sofisticated search on the resolver data
            // Locate resource providers
            logger.debug("Looking up resources for compact ID '{}', prefix '{}' and ID '{}'", compactId.getOriginal(), compactId.getPrefix(), compactId.getId());
            List<Resource> resources = resolverDataFetcher.findResourcesByPrefix(compactId.getPrefix());
            logger.info("CompactId '{}', with prefix '{}' got #{} resources back from the data backend", compactId
                    .getOriginal(), compactId.getPrefix(), resources.size());
            // Default behaviour for the Resolver Web Service is to return all the possible options, we may want to include
            // information regarding availability of every possible resource providing information on the given compact ID
            if (resources.isEmpty()) {
                // If no providers, produce error response
                response.setErrorMessage(String.format("No providers found for Compact ID '%s'", compactId.getOriginal()));
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            } else {
                // Resolve the links
                response.getPayload()
                        .setResolvedResources(resolverDataHelper.resolveResourcesForCompactId(compactId, resources));
                response.setHttpStatus(HttpStatus.OK);
            }
            // NOTE - This code may be refactored later
            response.setApiVersion(ApiCentral.apiVersion);
        }
        return response;
    }

    private ServiceResponseResolve resolveCompactId(String compactIdParameter, String selector) throws ResolverApiException {
        logger.info("Resolve Compact ID '{}', with selector '{}'", compactIdParameter, selector);
        ServiceResponseResolve response = createDefaultResponse();
        CompactId compactId = getCompactIdentifier(compactIdParameter, response);
        if (compactId != null) {
            // TODO - I do need to do something about prefix being 'null', otherwise it will break this first implementation
            // Locate resource providers
            // TODO - Refactor this later to make sure that 'selector' is always used lower case
            if ((compactId.getPrefix() == null) || (compactId.getPrefix().equals(selector.toLowerCase()))) {
                // This is normal resolution
                // TODO - Don't worry intrepid optimizer, this model will get refactored later on
                return resolveCompactId(CompactId.getCompactIdString(selector, compactId.getId()));
            } else {
                // We need to locate resources for the given compact ID and filter by the given selector
                // So... it turns out that I don't need anything more complex as a decider right now
                logger.debug("Looking up resources for compact ID '{}', selector '{}' and ID '{}'",
                        compactId.getOriginal(),
                        selector);
                List<Resource> resources = resolverDataFetcher.findResourcesByPrefix(compactId.getPrefix())
                        .stream()
                        .filter(resource -> {
                            if (resource.getProviderCode() != null) {
                                return resource.getProviderCode().equals(selector.toLowerCase());
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
                logger.info("CompactId '{}', with selector '{}' got #{} resources back from the data backend",
                        compactId.getOriginal(),
                        selector,
                        resources.size());
                if (resources.isEmpty()) {
                    // If no providers, produce error response
                    response.setErrorMessage(String.format("No providers found for Compact ID '%s', selector '%s'",
                            compactId.getOriginal(),
                            selector));
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                } else {
                    // Resolve the links
                    response.getPayload()
                            .setResolvedResources(resolverDataHelper.resolveResourcesForCompactId(compactId,resources));
                    response.setHttpStatus(HttpStatus.OK);
                }
            }
        }
        return response;
    }
}
