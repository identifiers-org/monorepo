package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.data.models.NamespaceStatistics;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/***
 * Relevant matomo documentation:
 * - <a href="https://developer.matomo.org/api-reference/reporting-api">Report API specification</a>
 * - <a href="https://developer.matomo.org/api-reference/reporting-api-segmentation">Segment API specification</a>
 */

@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Value("${org.identifiers.matomo.enabled}")
    boolean matomoIsEnabled;

    @Value("${org.identifiers.matomo.baseUrl}")
    String matomoBaseUrl;

    @Value("${org.identifiers.matomo.authToken}")
    String matomoTokenAuth;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    NamespaceRepository namespaceRepository;

    private String getMatomoRequestUrl(String prefix) {
        String matomoRequestUrl = UriComponentsBuilder.fromHttpUrl(matomoBaseUrl.trim())
                .queryParam("module", "API")
                .queryParam("method", "VisitsSummary.get")
                .queryParam("idSite", "1")
                .queryParam("period", "month")
                .queryParam("date", "today")
                .queryParam("format", "json")
                .queryParam("token_auth", matomoTokenAuth)
                .queryParam("segment", String.format("dimension6==%s", prefix))
                .build().toString();
        log.debug("Matomo query URL: {}", matomoRequestUrl);
        return matomoRequestUrl;
    }

    @GetMapping("/namespace/{prefix}")
    public ResponseEntity<ServiceResponse<NamespaceStatistics>> getNamespaceStatistic2(@PathVariable String prefix) {
        log.debug("Request for statistics of namespace {}", prefix);
        if (!matomoIsEnabled) {
            log.debug("Matomo disabled, answering with no content");
            return ResponseEntity.noContent().build();
        }
        if (namespaceRepository.findByPrefix(prefix) == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String matomoRequestUrl = getMatomoRequestUrl(prefix);
            NamespaceStatistics stats = restTemplate.getForObject(matomoRequestUrl, NamespaceStatistics.class);
            ServiceResponse<NamespaceStatistics> response = ServiceResponse.getBaseResponse(stats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RestClientException e) {
            ServiceResponse response = ServiceResponse.getBaseResponse();
            log.error("Error on matomo get", e);
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
