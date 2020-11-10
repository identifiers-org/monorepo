package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiCoveragePayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiInteroperabilityPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiCoverageResponse;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiInteroperabilityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: idorg-hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2020-11-10 10:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Associated model for FairApiController
 */
@Component
public class FairApiModel {
    // TODO
    public FairApiCoverageResponse checkForLui(FairApiCoveragePayload payload) {
        FairApiCoverageResponse response = new FairApiCoverageResponse();
        // TODO
        // WARNING - NOT IMPLEMENTED
        response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        return response;
    }

    public FairApiInteroperabilityResponse getCompactIdentifier(FairApiInteroperabilityPayload payload) {
        // TODO
        FairApiInteroperabilityResponse response = new FairApiInteroperabilityResponse();
        // TODO
        // WARNING - NOT IMPLEMENTED
        response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        return response;
    }

    public FairApiInteroperabilityResponse getInteroperabilityUrl(FairApiInteroperabilityPayload payload) {
        // TODO
        FairApiInteroperabilityResponse response = new FairApiInteroperabilityResponse();
        // TODO
        // WARNING - NOT IMPLEMENTED
        response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        return response;
    }
}
