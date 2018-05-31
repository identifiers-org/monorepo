package org.identifiers.cloud.ws.linkchecker.api.models;

import org.identifiers.cloud.ws.linkchecker.api.ApiCentral;
import org.identifiers.cloud.ws.linkchecker.api.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.ws.linkchecker.api.responses.ServiceResponseScoringRequest;
import org.identifiers.cloud.ws.linkchecker.api.responses.ServiceResponseScoringRequestPayload;
import org.identifiers.cloud.ws.linkchecker.models.HistoryTracker;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.models
 * Timestamp: 2018-05-21 10:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
public class LinkScoringApiModel {
    private static final Logger logger = LoggerFactory.getLogger(LinkScoringApiModel.class);

    @Autowired
    private HistoryTrackingService historyTrackingService;

    private ServiceResponseScoringRequest getDefaultResponse() {
        ServiceResponseScoringRequest response = new ServiceResponseScoringRequest();
        response.setApiVersion(ApiCentral.apiVersion);
        response.setPayload(new ServiceResponseScoringRequestPayload());
        return response;
    }

    public ServiceResponseScoringRequest getScoreForProvider(ScoringRequestWithIdPayload request) {
        logger.info("Provider scoring request for ID '{}', URL '{}'", request.getId(), request.getUrl());
        ServiceResponseScoringRequest response = getDefaultResponse();
        try {
            response.getPayload().setScore((int) Math.round(historyTrackingService.getTrackerForProvider(request)
                    .getHistoryStats(HistoryTracker.HistoryStats.SIMPLE).getUpPercenetage()));
        } catch (Exception e) {
            response.setErrorMessage(String.format("Scoring could not be calculated due to '%s'", e.getMessage()));
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
