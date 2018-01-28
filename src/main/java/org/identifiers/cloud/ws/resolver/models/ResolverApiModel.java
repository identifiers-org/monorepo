package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ResolverApiModel {
    private static Logger logger = LoggerFactory.getLogger(ResolverApiModel.class);

    @Autowired
    private ResolverDataFetcher resolverDataFetcher;

    // This code may be refactored out later on
    private List<ResolverApiResponseResource> resolveResourcesForCompactId(CompactId compactId, List<ResourceEntry>
            resourceEntries) {
        List<ResolverApiResponseResource> resolverApiResponseResources = resourceEntries
                .parallelStream()
                .map(resourceEntry -> {
                    ResolverApiResponseResource resolverApiResponseResource = new ResolverApiResponseResource();
                    resolverApiResponseResource.setInfo(resourceEntry.getInfo());
                    resolverApiResponseResource.setInstitution(resourceEntry.getInstitution());
                    resolverApiResponseResource.setLocation(resourceEntry.getLocation());
                    resolverApiResponseResource.setAccessUrl(resourceEntry
                            .getAccessURL().replace("{$id}", compactId.getId()));
                    return resolverApiResponseResource;
                }).collect(Collectors.toList());
        return resolverApiResponseResources;
    }

    // TODO - Document this API method
    public ResolverApiResponse resolveCompactId(String compactIdParameter) throws ResolverApiException {
        CompactId compactId = null;
        try {
            compactId = new CompactId(compactIdParameter);
        } catch (CompactIdException e) {
            throw new ResolverApiException(e.getMessage());
        }
        // Prepare default answer
        ResolverApiResponse resolverApiResponse = new ResolverApiResponse();
        resolverApiResponse.setResolvedResources(new ArrayList<>());
        // TODO - Check if prefix is null, as we may want to perform a more sofisticated search on the resolver data
        // Locate resource providers
        logger.debug("Looking up resources for compact ID '{}', prefix '{}' and ID '{}'", compactId.getOriginal(), compactId.getPrefix(), compactId.getId());
        List<ResourceEntry> resourceEntries = resolverDataFetcher.findResourcesByPrefix(compactId.getPrefix());
        logger.info("CompactId '{}', with prefix '{}' got #{} resources back from the data backend", compactId
                .getOriginal(), compactId.getPrefix(), resourceEntries.size());
        // Default behaviour for the Resolver Web Service is to return all the possible options, we may want to include
        // information regarding availability of every possible resource providing information on the given compact ID
        if (resourceEntries.isEmpty()) {
            // If no providers, produce error response
            resolverApiResponse.setErrorMsg(String.format("No providers found for Compact ID '%s'", compactId.getOriginal()));
            resolverApiResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        } else {
            // Resolve the links
            resolverApiResponse.setResolvedResources(resolveResourcesForCompactId(compactId, resourceEntries));
            resolverApiResponse.setHttpStatus(HttpStatus.OK);
        }
        // NOTE - This code may be refactored later
        return resolverApiResponse;
    }

    public ResolverApiResponse resolveCompactId(String selector, String compactIdParameter) throws ResolverApiException {
        CompactId compactId = null;
        try {
            compactId = new CompactId(compactIdParameter);
        } catch (CompactIdException e) {
            throw new ResolverApiException(e.getMessage());
        }
        // Prepare default answer
        ResolverApiResponse resolverApiResponse = new ResolverApiResponse();
        resolverApiResponse.setResolvedResources(new ArrayList<>());
        // TODO - Locate resource providers
        if ((compactId.getPrefix() == null) || (compactId.getPrefix().equals(selector))) {
            // This is normal resolution
            return resolveCompactId(compactIdParameter);
        } else {
            // TODO - We need to locate resources for the given compact ID and filter by the given selector
        }
        return resolverApiResponse;
    }
}
