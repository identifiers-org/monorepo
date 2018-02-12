package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@Component
public class MetadataApiModel {

    private IdResolver idResolver;
    private MetadataFetcher metadataFetcher;

    public MetadataApiModel(IdResolver idResolver, MetadataFetcher metadataFetcher) {
        this.idResolver = idResolver;
        this.metadataFetcher = metadataFetcher;
    }

    public MetadataApiResponse getMetadataFor(String compactId) {
        // TODO - Resolve the Compact ID
        List<ResolverApiResponseResource> resources = new ArrayList<>();
        try {
            idResolver.resolve(compactId);
        } catch (IdResolverException e) {
            // TODO
        }
        // TODO - Select the provider
        // TODO - Extract the metadata
        // TODO - return the response
        return null;
    }
}
