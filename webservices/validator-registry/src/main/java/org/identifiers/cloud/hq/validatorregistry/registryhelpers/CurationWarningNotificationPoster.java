package org.identifiers.cloud.hq.validatorregistry.registryhelpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.collections.CollectionUtils;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurationWarningNotificationPoster {
    private final RestTemplate restTemplateInternalAuthEnabled;

    @Value("${org.identifiers.cloud.registry.notification-endpoint}")
    URI notificationEndpoint;

    public void post(List<CurationWarningNotification> notifications) {
        if (CollectionUtils.isEmpty(notifications)) {
            log.info("Zero notifications to post");
            return;
        }
        log.info("Posting {} notifications to {}", notifications.size(), notificationEndpoint);

        var serviceRequest = ServiceRequest.of(notifications);
        try {
            var response = restTemplateInternalAuthEnabled.postForEntity(notificationEndpoint, serviceRequest, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Unsuccessful response code from registry: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to send notifications to {}: {}", notificationEndpoint, e.getMessage());
            log.debug("Stacktrace", e);
        }

        log.debug("Finished posting notifications");
    }
}
