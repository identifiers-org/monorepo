package org.identifiers.cloud.ws.sparql.data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public final class ResolutionDatasetUpdater implements Runnable {
    @Value("${org.identifiers.cloud.ws.sparql.updater.resolverDatasetUrl}")
    URI resolutionDatasetUri;
    @Value("${org.identifiers.cloud.ws.sparql.updater.timeInterval}")
    Duration timeInterval;

    private final RestTemplate restTemplate;
    private final ScheduledExecutorService updateExecutorService;
    private final Collection<ResolverDatasetSubscriber> subscribers;

    private final ParameterizedTypeReference<ServiceResponse<ResolverDatasetPayload>>
            resolutionDatasetTypeRef = new ParameterizedTypeReference<>() {};

    @PostConstruct
    public void schedulePeriodicRuns() {
        this.run();
        var secondsInterval = timeInterval.getSeconds();
        updateExecutorService.scheduleAtFixedRate(this, secondsInterval, secondsInterval, SECONDS);
    }

    @Override
    public void run() {
        log.debug("Updating resolve dataset from {}", resolutionDatasetUri);
        try {
            var resolutionDatasetResponse = restTemplate.exchange(resolutionDatasetUri,
                    HttpMethod.GET, null, resolutionDatasetTypeRef);
            if (resolutionDatasetResponse.getStatusCode().equals(HttpStatus.OK)) {
                var resolutionDataset = Objects.requireNonNull(resolutionDatasetResponse.getBody()).getPayload();
                subscribers.forEach(s -> s.receive(resolutionDataset));
            } else {
                log.error("Failed to update resolution data with HTTP code: {}",
                        resolutionDatasetResponse.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Failed to update resolution data", e);
        }
        log.info("Resolution dataset updated successfully");
    }
}
