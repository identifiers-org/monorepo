package org.identifiers.org.cloud.ws.metadata.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.identifiers.org.cloud.ws.metadata.api.ApiCentral;
import org.identifiers.org.cloud.ws.metadata.api.requests.ServiceRequestFetchMetadataForUrl;
import org.identifiers.org.cloud.ws.metadata.api.responses.ResponseFetchMetadataForUrlPayload;
import org.identifiers.org.cloud.ws.metadata.api.responses.ResponseFetchMetadataPayload;
import org.identifiers.org.cloud.ws.metadata.api.responses.ServiceResponseFetchMetadata;
import org.identifiers.org.cloud.ws.metadata.api.responses.ServiceResponseFetchMetadataForUrl;
import org.identifiers.org.cloud.ws.metadata.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(MetadataApiModel.class);
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

    private ServiceResponseFetchMetadata createDefaultResponseFetchMetadata(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadata response = new ServiceResponseFetchMetadata();
        response.setApiVersion(ApiCentral.apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseFetchMetadataPayload().setMetadata(""));
        return response;
    }

    private ServiceResponseFetchMetadataForUrl createDefaultResponseFetchMetadataForUrl(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadataForUrl response = new ServiceResponseFetchMetadataForUrl();
        response.setApiVersion(ApiCentral.apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        ResponseFetchMetadataForUrlPayload payload = new ResponseFetchMetadataForUrlPayload();
        payload.setMetadata("");
        response.setPayload(payload);
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

    private ResolvedResource resolveCompactId(String provider, String compactId, ServiceResponseFetchMetadata response) {
        try {
            // TODO
        }
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
        ServiceResponseFetchMetadata response = createDefaultResponseFetchMetadata(HttpStatus.OK, "");
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
            if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.INTERNAL_ERROR.getValue()) {
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.METADATA_NOT_FOUND.getValue()) {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            } else if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.METADATA_INVALID.getValue()) {
                response.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
        }
        // return the response
        return response;
    }

    public ServiceResponseFetchMetadata getMetadataFor(String selector, String compactId) {
        ServiceResponseFetchMetadata response = createDefaultResponseFetchMetadata(HttpStatus.OK, "");
        ResolvedResource resource = ;
    }

    public ServiceResponseFetchMetadataForUrl getMetadataForUrl(ServiceRequestFetchMetadataForUrl request) {
        // TODO - Check API version?
        String url = request.getPayload().getUrl();
        // Prepare default response
        ServiceResponseFetchMetadataForUrl response =
                createDefaultResponseFetchMetadataForUrl(HttpStatus.OK, "");
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
}
