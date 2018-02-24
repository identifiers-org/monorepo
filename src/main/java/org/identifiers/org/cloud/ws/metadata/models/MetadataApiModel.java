package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@Component
public class MetadataApiModel {

    private static String runningSessionId = UUID.randomUUID().toString();
    private IdResolver idResolver;
    private MetadataFetcher metadataFetcher;
    private IdResourceSelector idResourceSelector;

    public MetadataApiModel(IdResolver idResolver, MetadataFetcher metadataFetcher, IdResourceSelector idResourceSelector) {
        this.idResolver = idResolver;
        this.metadataFetcher = metadataFetcher;
        this.idResourceSelector = idResourceSelector;
    }

    public MetadataApiResponse getMetadataFor(String compactId) {
        List<ResolverApiResponseResource> resources = new ArrayList<>();
        MetadataApiResponse response = new MetadataApiResponse();
        String metadata = "";
        response.setMetadata(metadata);
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
        if (resources.isEmpty()) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s' " +
                    "because NO RESOURCES COULD BE FOUND", compactId));
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            return response;
        }
        // Select the provider
        ResolverApiResponseResource selectedResource = null;
        try {
            selectedResource = idResourceSelector.selectResource(resources);
        } catch (IdResourceSelectorException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                            "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        // Extract the metadata
        try {
            metadata = metadataFetcher.fetchMetadataFor(selectedResource.getAccessUrl());
        } catch (MetadataFetcherException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                            "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        response.setMetadata(metadata);
        // return the response
        return response;
    }

    public MetadataApiResponse getMetadataForUrl(String url) {
        // Prepare default response
        MetadataApiResponse response = new MetadataApiResponse();
        String metadata = "";
        response.setMetadata(metadata);
        // Extract the metadata
        try {
            metadata = metadataFetcher.fetchMetadataFor(url);
        } catch (MetadataFetcherException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for URL '%s', " +
                            "because '%s'",
                    url,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        response.setMetadata(metadata);
        // return the response
        return response;
    }

    public String livenessCheck() {
        return runningSessionId;
    }


}
