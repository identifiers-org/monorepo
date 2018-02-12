package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.http.HttpStatus;
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
        List<ResolverApiResponseResource> resources = new ArrayList<>();
        MetadataApiResponse response = new MetadataApiResponse();
        // Resolve the Compact ID
        try {
            resources = idResolver.resolve(compactId);
        } catch (IdResolverException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                    "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return response;
        }
        // TODO - Select the provider
        // TODO - Extract the metadata
        // TODO - return the response
        return response;
    }
}
