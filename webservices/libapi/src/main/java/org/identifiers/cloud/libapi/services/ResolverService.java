package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 10:04
 * ---
 *
 * This class implements a client to the Resolver service API.
 */
public class ResolverService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(ResolverService.class);
    // Re-try pattern, externalize this later if needed
    private final RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    private ResolverService() {

    }

    ResolverService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    /**
     * Helper method that submits a resolution HTTP GET request to the resolver service, and expects the corresponding
     * response.
     * @param serviceApiEndpoint service endpoint for the HTTP GET request.
     * @return resolution request response from the service or a guaranteed default response, where HTTP Status code and
     * error message contains information on what could have happened with the request.
     */
    private ServiceResponse<ResponseResolvePayload> doRequestResolution(String serviceApiEndpoint) {
        ServiceResponse<ResponseResolvePayload> response;
        try {
            var responseType = new ParameterizedTypeReference<ServiceResponse<ResponseResolvePayload>> () {};
            var requestResponse = retryTemplate.execute(retryContext -> {
                // Make the request
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(Configuration.responseErrorHandler());
                return restTemplate.exchange(serviceApiEndpoint, HttpMethod.GET, null, responseType);
            });
            response = requestResponse.getBody();
            if (response != null) {
                response.setHttpStatus(requestResponse.getStatusCode());
            }
           if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR resolving Compact ID " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        requestResponse.getStatusCode().value(),
                        requestResponse.getBody().getErrorMessage());
                logger.warn(errorMessage);
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

    // --- API ---
    /**
     * Resolver Service API: resolve a given Compact ID to a list of possible resources / providers of information on
     * that Compact ID.
     * @param compactId Compact ID to resolve.
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain infromation on
     * what could have happened to the request.
     */
    public ServiceResponse<ResponseResolvePayload> requestCompactIdResolution(String compactId) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, compactId);
        return doRequestResolution(serviceApiEndpoint);
    }

    /**
     * Resolver Service API: resolve a given Compact ID to a list of possible resources / providers of information on
     * that Compact ID, constraining the providers list to a single provider that matches the given selector.
     * @param compactId Compact ID to resolve.
     * @param selector this parameter will be used to select a provider from the result list of resolved resources.
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain infromation on
     * what could have happened to the request.
     */
    public ServiceResponse<ResponseResolvePayload> requestCompactIdResolution(String compactId, String selector) {
        String serviceApiEndpoint = String.format("%s/%s/%s", serviceApiBaseline, selector, compactId);
        return doRequestResolution(serviceApiEndpoint);
    }

    /**
     * Resolver Service API: given a raw request, i.e. everything that would go after the first '/' in the URL for the
     * Resolution API Service, request resolution for that raw request. This method is very useful for those cases where
     * we don't want to use the built-in models for building the request path from a given compact identifier with its
     * possible selector.
     * @param rawRequest the raw request for the Resolution API Service
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain information on
     * what could have happened to the request.
     */
    public ServiceResponse<ResponseResolvePayload> requestResolutionRawRequest(String rawRequest) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, rawRequest);
        return doRequestResolution(serviceApiEndpoint);
    }

    /**
     * Resolver Service API: get all the available resource providers resolved to a sample ID URL.
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain infromation on
     * what could have happened to the request.
     */
    public ServiceResponse<ResponseResolvePayload> getAllSampleIdsResolved() {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, "insightApi/get_all_sample_ids_resolved");
        return doRequestResolution(serviceApiEndpoint);
    }

    /**
     * Resolver Service API: get the Home URL for all available resource providers, within the context of each namespace
     * registered in the resolution service.
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain infromation on
     * what could have happened to the request.
     */
    public ServiceResponse<ResponseResolvePayload> getAllHomeUrls() {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, "insightApi/get_all_home_urls");
        return doRequestResolution(serviceApiEndpoint);
    }
}
