package org.identifiers.cloud.hq.ws.registry.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty("org.identifiers.matomo.warning-scorer.enabled")
public class UsageScoreHelperBasedOnMatomo {
    @Value("${org.identifiers.matomo.baseUrl}")
    URI baseMatomoUri;

    @Value("${org.identifiers.matomo.warning-scorer.dimension-id}")
    int dimensionId;

    @Value("${org.identifiers.matomo.warning-scorer.site-id}")
    int siteId;

    @Value("${org.identifiers.matomo.authToken}")
    String token;

    private final RestTemplate matomoRestTemplate;

    @Getter
    private Map<String, Long> scoresPerNamespace = Map.of();

    @PostConstruct
    public void downloadDataset() {
        URI requestUri = UriComponentsBuilder.fromUri(baseMatomoUri)
                .queryParam("date", "lastMonth")
                .queryParam("module", "API")
                .queryParam("format", "json")
                .queryParam("method", "CustomDimensions.getCustomDimension")
                .queryParam("idDimension", dimensionId)
                .queryParam("idSite", siteId)
                .queryParam("period", "month")
                .queryParam("filter_limit", "1000")
                .build().toUri();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("token_auth", token);
        var request = new HttpEntity<>(requestBody, headers);
        var typeRef = new ParameterizedTypeReference<List<MatomoResponseEntry>>() {};

        try {
            var response = matomoRestTemplate.exchange(requestUri, HttpMethod.POST, request, typeRef);

            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    scoresPerNamespace = response.getBody().stream()
                            .collect(Collectors.toMap(
                                    MatomoResponseEntry::label,
                                    MatomoResponseEntry::nbVisits
                            ));
                } else {
                    log.error("Failed get usage from matomo: null body");
                }
            } else {
                log.error("Failed get usage from matomo: {}", response.getStatusCode());
                log.debug("Body: {}", response.getBody());
            }
        } catch (RestClientException e) {
            log.error("Failed get usage from matomo: {}", e.getMessage());
            log.debug("Stacktrace:", e);
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record MatomoResponseEntry (
            long nbVisits,
            String label
    ) {}
}
