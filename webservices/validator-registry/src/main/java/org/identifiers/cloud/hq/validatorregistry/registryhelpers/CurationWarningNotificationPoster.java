package org.identifiers.cloud.hq.validatorregistry.registryhelpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        log.info("Posting {} notifications to {}", notifications.size(), notificationEndpoint);

        var serviceRequest = ServiceRequest.of(notifications);
        try {
            restTemplateInternalAuthEnabled.postForEntity(notificationEndpoint, serviceRequest, Void.class);
        } catch (Exception e) {
            log.error("Failed to send notifications to {}", notificationEndpoint, e);
        }

        log.debug("Finished posting notifications");
    }
}
