package org.identifiers.cloud.hq.ws.registry.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 7;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds
    // RestTemplate configuration
    // TODO Maybe refactor this in the future
    private static final int WS_REQUEST_CONNECT_TIMEOUT = 2000; // 2 seconds
    private static final int WS_REQUEST_READ_TIMEOUT = 2000; // 2 seconds
    // Configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.ror.api.baseurl}")
    private String rorApiBaseUrl;
    @Value("${org.identifiers.cloud.hq.ws.registry.ror.api.urlsuffix.organization}")
    private String rorApiUrlSuffixOrganization;

    // Factory method
    private RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Configure requests time outs
        simpleClientHttpRequestFactory.setConnectTimeout(WS_REQUEST_CONNECT_TIMEOUT);
        simpleClientHttpRequestFactory.setReadTimeout(WS_REQUEST_READ_TIMEOUT);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Retryable(maxAttempts = WS_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = WS_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException {
        // TODO
        // TODO - Run some ROR ID validation first... maybe?
        String queryUrl = String.format("%s/%s/%s", rorApiBaseUrl, rorApiUrlSuffixOrganization, rorId);
        log.info(String.format("Fetching Organization Information for ROR ID '%s', query URL '%s'", rorId, queryUrl));
        ResponseEntity<?> response = getRestTemplate().getForEntity(queryUrl, Organization.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                log.info(String.format("For ROR ID '%s', de-serialized object '%s'", rorId, objectMapper.writeValueAsString(response.getBody())));
            } catch (JsonProcessingException e) {
                // TODO ignore
                e.printStackTrace();
            }
            if (!response.hasBody()) {
                String errorMessage = String.format("Organization information fetch for ROR ID '%s' came back with EMTPY RESPONSE BODY", rorId);
                log.error(errorMessage);
                throw new RorOrgApiServiceException(errorMessage);
            }
            return (Organization) response.getBody();
        }
        // There was an error
        String errorMessage = String.format("Organization information fetch for ROR ID '%s' came back with HTTP STATUS '%d'", rorId, response.getStatusCodeValue());
        log.error(errorMessage);
        throw new RorOrgApiServiceException(errorMessage);
    }
}
