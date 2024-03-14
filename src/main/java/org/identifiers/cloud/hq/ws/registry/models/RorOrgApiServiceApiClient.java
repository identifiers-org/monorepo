package org.identifiers.cloud.hq.ws.registry.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.util.Collections;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-15 01:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class RorOrgApiServiceApiClient implements RorOrgApiService {
    // Re-try configuration
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 3;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 500; // 1.5 seconds
    // RestTemplate configuration
    // TODO Maybe refactor this in the future
    private static final int WS_REQUEST_CONNECT_TIMEOUT = 500; // 0.5 seconds
    private static final int WS_REQUEST_READ_TIMEOUT = 500; // 0.5 seconds
    // Configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.ror.api.baseurl}")
    private String rorApiBaseUrl;
    @Value("${org.identifiers.cloud.hq.ws.registry.ror.api.query.suffix.organizations}")
    private String rorApiUrlSuffixOrganization;

    // Factory method
    private RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Configure requests time outs
        simpleClientHttpRequestFactory.setConnectTimeout(WS_REQUEST_CONNECT_TIMEOUT);
        simpleClientHttpRequestFactory.setReadTimeout(WS_REQUEST_READ_TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return restTemplate;
    }

    private String getEncodedQueryPath(String rorId) throws RorOrgApiServiceException {
        try {
            return UriUtils.encodePath(rorId, "UTF-8");
        } catch (RuntimeException e) {
            String errorMessage = String.format("FAILED encode ROR ID '%s'", rorId);
            log.error(errorMessage);
            throw new RorOrgApiServiceException(errorMessage);
        }
    }

    @Retryable(maxAttempts = WS_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = WS_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException {
        // TODO
        // TODO - Run some ROR ID validation first... maybe?
        // TODO - If 404, do not re-try
        String queryUrl = String.format("%s/%s/%s", rorApiBaseUrl, rorApiUrlSuffixOrganization, getEncodedQueryPath(rorId));
        log.info(String.format("Fetching Organization Information for ROR ID '%s', query URL '%s'", rorId, queryUrl));
        try {
            //ResponseEntity<Organization> response = getRestTemplate().getForEntity(new URI(queryUrl), Organization.class);
            ResponseEntity<Organization> response = getRestTemplate().exchange(new URI(queryUrl), HttpMethod.GET, null, Organization.class);
            log.info(String.format("Fetching Organization Information for ROR ID '%s', query URL '%s', response code '%d'", rorId, queryUrl, response.getStatusCode().value()));
            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                log.info(String.format("For ROR ID '%s', response body '%s'", rorId, objectMapper.writeValueAsString(response.getBody())));
                if (!response.hasBody()) {
                    String errorMessage = String.format("Organization information fetch for ROR ID '%s' came back with EMTPY RESPONSE BODY", rorId);
                    log.error(errorMessage);
                    throw new RorOrgApiServiceException(errorMessage);
                }
                return response.getBody();
            }
            // There was an error
            String errorMessage = String.format("Organization information fetch for ROR ID '%s' came back with HTTP STATUS '%d'", rorId, response.getStatusCode().value());
            log.error(errorMessage);
            throw new RorOrgApiServiceException(errorMessage);
        } catch (RorOrgApiServiceException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = String.format("Organization information fetch for ROR ID '%s' FAILED due to the following error, '%s'", rorId, e.getMessage());
            log.error(errorMessage);
            throw new RorOrgApiServiceException(errorMessage);
        }
    }
}
