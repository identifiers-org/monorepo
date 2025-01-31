package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.metadata.*;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.metadata.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpMethod.GET;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 13:42
 * ---
 *
 * This class implements a cient to the Metadata service API.
 */
public class MetadataService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    // Re-try pattern, externalize this later if needed
    private final RetryTemplate retryTemplate = Configuration.retryTemplate();
    private final String serviceApiBaseline;

    MetadataService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    /**
     * This helper method sends an HTTP GET request to the given service endpoint, expecting a metadata fetch request response
     * back from it.
     * @param serviceApiEndpoint this is the endpoint URL where to submit the request to.
     * @return a metadata fetch request response.
     */
    private ServiceResponse<ResponseFetchMetadataPayload> doRequestFetchMetadata(String serviceApiEndpoint) {
        var payload = new ResponseFetchMetadataPayload().setMetadata("");
        var response = ServiceResponse.of(payload);
        try {
            var typeRef = new ParameterizedTypeReference<ServiceResponse<ResponseFetchMetadataPayload>>() {};
            var requestResponse = retryTemplate.execute(retryContext -> {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(Configuration.responseErrorHandler());
                return restTemplate.exchange(serviceApiEndpoint, GET, null, typeRef);
            });
            response = requestResponse.getBody();
           if (response != null) {
              response.setHttpStatus(requestResponse.getStatusCode().value());
           }
           if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR fetching metadata " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        requestResponse.getStatusCode().value(),
                        requestResponse.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            // Make sure we return a default response in case anything bad happens
            String errorMessage = String.format("ERROR resolving Compact ID at '%s' " +
                    "because of '%s'", serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = ServiceResponse.ofError(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    /**
     * This helper method prepares the HTTP POST request for fetching metadata from a given URL.
     * @param url URL to fetch metadata from.
     * @param serviceApiEndpoint service endpoint URL for this request.
     * @return a request entity with all set to go, including the request body.
     */
    private RequestEntity<ServiceRequest<RequestFetchMetadataForUrlPayload>>
    prepareRequestFetchMetadataForUrl(String url, String serviceApiEndpoint) {
        // Prepare the request body
        var payload = new RequestFetchMetadataForUrlPayload().setUrl(url);
        var requestBody = ServiceRequest.of(payload);
        // Prepare the request entity
        RequestEntity<ServiceRequest<RequestFetchMetadataForUrlPayload>> request = null;
        try {
            request = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return request;
    }

    /**
     * This is a helper method that will actually submit the HTTP POST request of metadata fetch for URL, already
     * prepared, and it will expect the right response from the service.
     * @param request already prepared metadata fetch request for URL
     * @return response from the service
     */
    private ResponseEntity<ServiceResponse<RequestFetchMetadataForUrlPayload>>
    makeRequestFetchMetadataForUrl(RequestEntity<ServiceRequest<RequestFetchMetadataForUrlPayload>> request) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        var typeRef = new ParameterizedTypeReference<ServiceResponse<RequestFetchMetadataForUrlPayload>>() {};
        return restTemplate.exchange(request, typeRef);
    }

    // --- API ---

    /**
     * Metadata API Service: get metadata information for a given Compact ID.
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param compactId Compact ID for which to fetch metadata information.
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponse<ResponseFetchMetadataPayload> getMetadataForCompactId(String compactId) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, compactId);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata API Service: get metadata information for a given Compact ID, from the provider specified by the given
     * 'selector' / 'provider code'
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param compactId Compact ID for which to fetch metadata information.
     * @param selector provider code of the resource where the metadata should be extracted from
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponse<ResponseFetchMetadataPayload> getMetadataForCompactId(String compactId, String selector) {
        String serviceApiEndpoint = String.format("%s/%s/%s", serviceApiBaseline, selector, compactId);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata API Service: get metadata information for the given raw request.
     * This method is very useful for those cases where we don't want to use the built-in models for building the
     * request path from a given compact identifier with its possible selector.
     * @param rawRequest raw request
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponse<ResponseFetchMetadataPayload> getMetadataForRawRequest(String rawRequest) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, rawRequest);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata Service API: get metadata information for a given URL.
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param url URL from which metadata wants to be extracted.
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponse<RequestFetchMetadataForUrlPayload> getMetadataForUrl(String url) {
        String serviceApiEndpoint = String.format("%s/getMetadataForUrl", serviceApiBaseline);
        var payload = new RequestFetchMetadataForUrlPayload();
        var response = ServiceResponse.of(payload);
        logger.info("Requesting metadata for URL '{}' at service '{}'", url, serviceApiEndpoint);
        // Prepare the request
        var request = prepareRequestFetchMetadataForUrl(url, serviceApiEndpoint);
        try {
            var requestResponse = retryTemplate.execute(retryContext -> {
                // Do the actual request
                if (request != null) {
                    return makeRequestFetchMetadataForUrl(request);
                }
                // If we get here, send back a custom-made error response
                ServiceResponse<RequestFetchMetadataForUrlPayload> errorServiceResponse = ServiceResponse
                      .ofError(HttpStatus.BAD_REQUEST, String.format("INVALID URI %s", serviceApiEndpoint));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorServiceResponse);
            });
            // Set the response to return to the client
            response = requestResponse.getBody();
            // Set actual HTTP Status in the response body
            if (response != null) {
                response.setHttpStatus(requestResponse.getStatusCode());
            }
            if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR retrieving metadata for URL '%s' " +
                                                          "at '%s', " +
                                                          "HTTP status code '%d', " +
                                                          "explanation '%s'",
                                                    url,
                                                    serviceApiEndpoint,
                                                    requestResponse.getStatusCode().value(),
                                                    requestResponse.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
            return response;
        } catch (RuntimeException e) {
            // Make sure we return a default response in case anything bad happens
            String errorMessage = String.format("ERROR retrieving resource recommendations from '%s' " +
                                                      "because of '%s'", serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            return ServiceResponse.ofError(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }
}
