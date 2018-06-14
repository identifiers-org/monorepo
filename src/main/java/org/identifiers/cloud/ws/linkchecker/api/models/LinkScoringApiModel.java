package org.identifiers.cloud.ws.linkchecker.api.models;

import org.identifiers.cloud.ws.linkchecker.api.ApiCentral;
import org.identifiers.cloud.ws.linkchecker.api.requests.ServiceRequestScoreProvider;
import org.identifiers.cloud.ws.linkchecker.api.requests.ServiceRequestScoring;
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

    // TODO - The following method is a very simple way of scoring a provider but, in the future, more complex logic
    // TODO - could be set in place, that also weights in 'how good' that provider has been when it comes to providing
    // TODO - resources.
    /**
     * Getting a score for a provider (within the context of a particular namespace), is based on the 'uptime' history
     * of that provider, i.e. how many times the provider was up over the total number of times we have checked its
     * status.
     * @param request the request that contains the reference to the provider being scored
     * @return a Service Response ready for the controller to send back to the client
     */
    public ServiceResponseScoringRequest getScoreForProvider(ServiceRequestScoreProvider request) {
        logger.info("Provider scoring request for ID '{}', URL '{}'",
                request.getPayload().getId(),
                request.getPayload().getUrl());
        ServiceResponseScoringRequest response = getDefaultResponse();
        try {
            response.getPayload()
                    .setScore((int) Math.round(historyTrackingService.getTrackerForProvider(request.getPayload())
                            .getHistoryStats(HistoryTracker.HistoryStats.SIMPLE).getUpPercenetage()));
        } catch (Exception e) {
            response.setErrorMessage(String.format("Scoring could not be calculated due to '%s'", e.getMessage()));
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponseScoringRequest getScoreForResolvedId(ServiceRequestScoreProvider request) {
        logger.info("Resource scoring request for ID '{}', URL '{}'",
                request.getPayload().getId(),
                request.getPayload().getUrl());
        ServiceResponseScoringRequest response = getDefaultResponse();
        try {
            response.getPayload().setScore((int) Math.round(historyTrackingService.getTrackerForResource(request
                    .getPayload()).getHistoryStats(HistoryTracker.HistoryStats.SIMPLE).getUpPercenetage()));
        } catch (Exception e) {
            response.setErrorMessage(String.format("Scoring could not be calculated due to '%s'", e.getMessage()));
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponseScoringRequest getScoreForUrl(ServiceRequestScoring request) {
        // TODO
    }
}
