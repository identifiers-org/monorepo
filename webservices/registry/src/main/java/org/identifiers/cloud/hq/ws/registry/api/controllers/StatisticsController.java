package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.NamespaceStatistics;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.api.models.StatisticsModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

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

    final StatisticsModel model;

    public StatisticsController(StatisticsModel model) {
        this.model = model;
    }

    @GetMapping("/namespace/{prefix}")
    public ResponseEntity<ServiceResponse<NamespaceStatistics>> getNamespaceStatistic(@PathVariable String prefix) {
        log.debug("Request for statistics of namespace {}", prefix);
        if (!matomoIsEnabled) {
            log.debug("Matomo disabled, answering with no content");
            return ResponseEntity.noContent().build();
        }
        if (!model.doesNamespaceExists(prefix)) {
            return ResponseEntity.notFound().build();
        }

        try {
            var stats = model.getMatomoStatisticsFor(prefix);
            log.debug("Found statistics for {}: {}", prefix, stats);
            var response = ServiceResponse.of(stats);
            return ResponseEntity.ok().body(response);
        } catch (RestClientException e) {
            if (log.isDebugEnabled()) {
                log.error("Error on matomo get", e);
            } else {
                log.error("Error on matomo get {}", e.getMessage());
            }
            ServiceResponse<NamespaceStatistics> response =
                        ServiceResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
