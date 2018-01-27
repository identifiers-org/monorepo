package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 10:31
 * ---
 */
public class ResolverApiModel {
    private static Logger logger = LoggerFactory.getLogger(ResolverApiModel.class);

    @Autowired
    private ResolverDataFetcher resolverDataFetcher;

    public String resolveCompactId(String compactIdParameter) throws ResolverApiException {
        CompactId compactId = null;
        try {
            compactId = new CompactId(compactIdParameter);
        } catch (CompactIdException e) {
            throw new ResolverApiException(e.getMessage());
        }
        // TODO - Check if prefix is null
        // Locate resource providers
        logger.info("Looking up resources for compact ID '{}', prefix '{}' and ID '{}'", compactId.getOriginal(), compactId.getPrefix(), compactId.getId());
        List<ResourceEntry> resourceEntries = resolverDataFetcher.findResourcesByPrefix(compactId.getPrefix());
        if (resourceEntries == null) {
            logger.error("CompactId '{}', with prefix '{}' got NULL resources back from the data backend", compactId
                    .getOriginal(), compactId.getPrefix());
        } else {
            logger.info("CompactId '{}', with prefix '{}' got #{} resources back from the data backend", compactId
                    .getOriginal(), compactId.getPrefix(), resourceEntries.size());
        }
        // TODO - If no providers, produce error response
        // TODO - If there are providers, transform them into Resolver API response providers
        return "";
    }

    public String resolveCompactId(String selector, String compactIdParameter) throws ResolverApiException {
        // TODO - Locate resource providers
        // TODO - If no providers, produce error response
        // TODO - If there are providers, apply filtering criteria
        // TODO - Return Resolver API response providers
        return "";
    }
}
