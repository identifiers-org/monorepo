package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.linkchecker.ScoringRequestWithIdPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseScoringRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-06-18 9:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This class implements a client for the Link Checker Service.
 */
public class LinkCheckerService {
    public static final String API_VERSION = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckerService.class);
    // Re-try pattern
    private final RetryTemplate retryTemplate = Configuration.retryTemplate();
    private final String serviceApiBaseline;

    LinkCheckerService(String serviceHost, String servicePort) {
        // TODO - This needs to be refactored in the future for supporting multiple schema (HTTP, HTTPS)
        serviceApiBaseline = String.format("http://%s:%s", serviceHost, servicePort);
    }

    /**
     * This helper will perform any preparation steps needed to get the service request ready for the payload. Right now
     * it only sets the API Version information
     *
     * @param request on which to perform the preparation steps
     */
    private void prepareScoringRequest(ServiceRequest<?> request) {
        request.setApiVersion(API_VERSION);
    }

    // --- API ---

    /**
     * Link Checker Service API: Get a Reliability Score for a given Provider ID, i.e. a provider in the context of a
     * namespace / prefix.
     * @param providerId ID of the provider
     * @param url URL of the given provider in the context of a particular namespace / prefix
     * @return a scoring response from the Link Checking Service
     */

    public ServiceResponse<ServiceResponseScoringRequestPayload> getScoreForProvider(String providerId, String url) {
        return getScoreForProvider(providerId, url, false);
    }

    public ServiceResponse<ServiceResponseScoringRequestPayload> getScoreForProvider(String providerId, String url, boolean isProtectedUrls) {
        String endpoint = String.format("%s/getScoreForProvider", serviceApiBaseline);

        // Prepare the request body
        var payload = new ScoringRequestWithIdPayload();
        var requestBody = ServiceRequest.of(payload);
        payload.setId(providerId).setUrl(url).setAccept401or403(isProtectedUrls);

        // Prepare the request entity
        RequestEntity<ServiceRequest<ScoringRequestWithIdPayload>> requestEntity;
        try {
            requestEntity = RequestEntity.post(new URI(endpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI ruing score for provider '{}'", endpoint);
            String msg = String.format("An error occurred while trying score " +
                                             "Provider with ID '%s', URL '%s', using service endpoint '%s' INVALID URI",
                                       providerId, url, endpoint);
            return ServiceResponse.ofError(HttpStatus.BAD_REQUEST, msg);
        }

        // Make the request using the re-try pattern
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        try {
            var typeRef = new ParameterizedTypeReference<ServiceResponse<ServiceResponseScoringRequestPayload>>() {};
            var responseEntity = retryTemplate.execute(retryContext ->
                restTemplate.exchange(requestEntity, typeRef)
            );
            var response = responseEntity.getBody();
            if (response != null) {
                response.setHttpStatus(responseEntity.getBody().getHttpStatus());
            }
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                "Provider ID '%s', URL '%s' from '%s', " +
                                "HTTP status code '%d', explanation '%s'",
                        providerId,
                        url,
                        endpoint,
                        responseEntity.getStatusCode().value(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
            return response;
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                            "Provider ID '%s', URL '%s' from '%s', explanation '%s'",
                    providerId,
                    url,
                    endpoint,
                    e.getMessage());
            return ServiceResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    ServiceResponse<ServiceResponseScoringRequestPayload> getScoreForResolvedId(String resourceId, String url) {
        return getScoreForResolvedId(resourceId, url, false);
    }

    public ServiceResponse<ServiceResponseScoringRequestPayload>
    getScoreForResolvedId(String resourceId, String url, boolean isProtectedUrls) {
        String endpoint = String.format("%s/getScoreForResolvedId", serviceApiBaseline);

        var payload = new ScoringRequestWithIdPayload();
        payload.setId(resourceId).setUrl(url).setAccept401or403(isProtectedUrls);
        var requestBody = ServiceRequest.of(payload);

        // Prepare the request entity
        RequestEntity<ServiceRequest<ScoringRequestWithIdPayload>> requestEntity;
        try {
            requestEntity = RequestEntity.post(new URI(endpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            String errMessage = String.format("An error occurred while trying score " +
                                                    "Resource with ID '%s', URL '%s', using service endpoint '%s' INVALID URI",
                                              resourceId, url, endpoint);
            logger.error("INVALID URI during score for resolved id '{}'", endpoint);
            return ServiceResponse.ofError(HttpStatus.BAD_REQUEST, errMessage);
        }

        // Prepare response
        ServiceResponse<ServiceResponseScoringRequestPayload> response;
        // Make the request using the re-try pattern
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        try {
            var typeRef = new ParameterizedTypeReference<ServiceResponse<ServiceResponseScoringRequestPayload>>() {};
            var responseEntity = retryTemplate.execute(retryContext ->
                restTemplate.exchange(requestEntity, typeRef)
            );
            response = responseEntity.getBody();
            if (response != null && responseEntity.getBody() != null) {
                response.setHttpStatus(responseEntity.getBody().getHttpStatus());
            }
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                "Resource ID '%s', URL '%s' " +
                                "from '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        resourceId,
                        url,
                        endpoint,
                        responseEntity.getStatusCode().value(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                            "Resource ID '%s', URL '%s' " +
                            "from '%s', " +
                            "explanation '%s'",
                    resourceId,
                    url,
                    endpoint,
                    e.getMessage());
            response = ServiceResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
            logger.error(errorMessage);
        }
        return response;
    }
}
