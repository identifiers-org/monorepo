package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiCoveragePayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiInteroperabilityPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiCoverageResponse;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiInteroperabilityResponse;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.helpers.MirIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private NamespaceRepository namespaceRepository;

    public FairApiCoverageResponse checkForLui(FairApiCoveragePayload payload) {
        FairApiCoverageResponse response = new FairApiCoverageResponse();
        if (MirIdHelper.isValid(payload.getLui())) {
            if ((resourceRepository.findByMirId(payload.getLui()) == null)
                    && (namespaceRepository.findByMirId(payload.getLui()) == null)) {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            }
        } else {
            // Invalid MIR ID
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
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
