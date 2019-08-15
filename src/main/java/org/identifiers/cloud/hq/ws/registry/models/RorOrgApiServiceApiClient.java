package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.beans.factory.annotation.Value;
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
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds
    // RestTemplate configuration
    // TODO Maybe refactor this in the future
    private static final int WS_REQUEST_CONNECT_TIMEOUT = 2000; // 2 seconds
    private static final int WS_REQUEST_READ_TIMEOUT = 2000; // 2 seconds

    // Configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.ror.api.baseurl}")
    private String rorApiBaseUrl;

    @Retryable(maxAttempts = WS_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = WS_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException {
        // TODO
        log.info(String.format("Fetching Organization Information for ROR ID '%s'", rorId));
        RestTemplate restTemplate = new RestTemplate();

        return null;
    }
}
