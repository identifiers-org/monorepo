package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.NamespaceStatistics;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Component
@Slf4j
public class StatisticsModel {
    @Value("${org.identifiers.matomo.baseUrl}")
    String matomoBaseUrl;

    @Value("${org.identifiers.matomo.authToken}")
    String matomoTokenAuth;

    final NamespaceRepository namespaceRepository;
    final RestTemplate restTemplate;
    public StatisticsModel(NamespaceRepository namespaceRepository, RestTemplateBuilder restTemplateBuilder) {
        this.namespaceRepository = namespaceRepository;
        restTemplate = restTemplateBuilder.setReadTimeout(Duration.ofMinutes(2)).build();
    }

    public boolean doesNamespaceExists(String prefix) {
        return namespaceRepository.findByPrefix(prefix) != null;
    }

    private String getMatomoRequestUrl(String prefix) {
        String matomoRequestUrl = UriComponentsBuilder.fromHttpUrl(matomoBaseUrl.trim())
                .queryParam("module", "API")
                .queryParam("method", "VisitsSummary.get")
                .queryParam("idSite", "1")
                .queryParam("period", "month")
                .queryParam("date", "lastMonth")
                .queryParam("format", "json")
                .queryParam("token_auth", matomoTokenAuth)
                .queryParam("segment", String.format("dimension6==%s", prefix))
                .build().toString();
        log.debug("Matomo query URL: {}", matomoRequestUrl);
        return matomoRequestUrl;
    }

    @Retryable(retryFor = RestClientException.class, maxAttempts = 5, backoff = @Backoff(maxDelay = 200))
    public NamespaceStatistics getMatomoStatisticsFor(String prefix) {
        String matomoRequestUrl = getMatomoRequestUrl(prefix);
        return restTemplate.getForObject(matomoRequestUrl, NamespaceStatistics.class);
    }
}
