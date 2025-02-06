package org.identifiers.cloud.hq.ws.registry.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

/**
 * This is a temporary service to directly get resources with low availability from the link checker service.
 *   This should be replaced by an actual curation service in the future.
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "org.identifiers.cloud.hq.ws.registry.services.link-checker.enabled", havingValue = "true")
public class LowAvailabilityOnLinkCheckerService {
    private final RestTemplate restTemplate;

    @Value("${org.identifiers.cloud.hq.ws.registry.services.link-checker.min-availability}")
    private int minAvailability;
    @Value("${org.identifiers.cloud.hq.ws.registry.services.link-checker.host}")
    private String linkCheckerHost;
    @Value("${org.identifiers.cloud.hq.ws.registry.services.link-checker.port}")
    private String linkCheckerPort;

    @Getter
    private Map<String, Float> resourcesToAvailabilityMap;

    private static final ParameterizedTypeReference<Map<String, Float>>
            RESPONSE_TYPE = new ParameterizedTypeReference<>() {};

    public LowAvailabilityOnLinkCheckerService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Scheduled(fixedRateString = "${org.identifiers.cloud.hq.ws.registry.services.link-checker.update-interval}")
    public void retrieveResourcesWithLowAvailability() {
        log.info("[BEGIN] Retrieving resources with low availability");
        try {
            var responseEntity = restTemplate.exchange("http://{host}:{port}/getResourcesWithLowAvailability?minAvailability={minAvailability}",
                    GET, null, RESPONSE_TYPE, linkCheckerHost, linkCheckerPort, minAvailability);
            resourcesToAvailabilityMap = responseEntity.getBody();
        } catch (ResourceAccessException ex) {
            if (log.isDebugEnabled()) {
                log.error("Failed to retrieve resources with low availability", ex);
            } else {
                log.error("Failed to retrieve resources with low availability: {}", ex.getMessage());
            }
        }
        if (resourcesToAvailabilityMap != null) {
            log.debug("Found {} resources", resourcesToAvailabilityMap.size());
        } else {
            log.debug("Found zero resources");
        }
        log.info("[ END ] Retrieving resources with low availability");
    }
}
