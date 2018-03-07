package org.identifiers.org.cloud.ws.metadata.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.identifiers.org.cloud.ws.metadata.models.api.responses.ResponseFetchMetadataPayload;
import org.identifiers.org.cloud.ws.metadata.models.api.responses.ServiceResponseFetchMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public static final String apiVersion = "1.0";
    private static Logger logger = LoggerFactory.getLogger(MetadataApiModel.class);
    private static String runningSessionId = UUID.randomUUID().toString();
    private IdResolver idResolver;
    private MetadataFetcher metadataFetcher;
    private IdResourceSelector idResourceSelector;

    public MetadataApiModel(IdResolver idResolver,
                            MetadataFetcher metadataFetcher,
                            IdResourceSelector idResourceSelector) {
        this.idResolver = idResolver;
        this.metadataFetcher = metadataFetcher;
        this.idResourceSelector = idResourceSelector;
    }

    private ServiceResponseFetchMetadata createDefaultResponse(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadata response = new ServiceResponseFetchMetadata();
        response.setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseFetchMetadataPayload().setMetadata(""));
        return response;
    }

    private List<ResolvedResource> resolveCompactId(String compactId, ServiceResponseFetchMetadata response) {
        // Resolve the Compact ID
        List<ResolvedResource> resources = new ArrayList<>();
        try {
            resources = idResolver.resolve(compactId);
        } catch (IdResolverException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                            "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return resources;
        }
        if (resources.isEmpty()) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s' " +
                    "because NO RESOURCES COULD BE FOUND", compactId));
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return resources;
    }

    private ResolvedResource selectResource(String compactId,
                                            List<ResolvedResource> resources,
                                            ServiceResponseFetchMetadata response) {
        ResolvedResource selectedResource = null;
        try {
            selectedResource = idResourceSelector.selectResource(resources);
        } catch (IdResourceSelectorException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                            "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return selectedResource;
        }
        // Log selection
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Mining metadata for Compact ID '{}' on selected resource '{}'",
                    compactId,
                    mapper.writeValueAsString(selectedResource));
        } catch (JsonProcessingException e) {
            // TODO will never happen ^_^
        }
        return selectedResource;
    }

    // --- API Methods ---
    public ServiceResponseFetchMetadata getMetadataFor(String compactId) {
        ServiceResponseFetchMetadata response = createDefaultResponse(HttpStatus.OK, "");
        List<ResolvedResource> resources = resolveCompactId(compactId, response);
        if (response.getHttpStatus() != HttpStatus.OK) {
            return response;
        }
        // Select the provider
        ResolvedResource selectedResource = selectResource(compactId, resources, response);
        if (response.getHttpStatus() != HttpStatus.OK) {
            return response;
        }
        // Extract the metadata
        try {
           response.getPayload().setMetadata(metadataFetcher.fetchMetadataFor(selectedResource.getAccessUrl()));
        } catch (MetadataFetcherException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for Compact ID '%s', " +
                            "because '%s'",
                    compactId,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            if (e.)
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        // return the response
        return response;
    }

    public ServiceResponseFetchMetadata getMetadataForUrl(String url) {
        // Prepare default response
        ServiceResponseFetchMetadata response = createDefaultResponse(HttpStatus.OK, "");
        // Extract the metadata
        try {
            response.getPayload().setMetadata(metadataFetcher.fetchMetadataFor(url));
        } catch (MetadataFetcherException e) {
            response.setErrorMessage(String.format("FAILED to fetch metadata for URL '%s', " +
                            "because '%s'",
                    url,
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        // return the response
        return response;
    }

    public String livenessCheck() {
        return runningSessionId;
    }

    public String readinessCheck() {
        return runningSessionId;
    }

}
