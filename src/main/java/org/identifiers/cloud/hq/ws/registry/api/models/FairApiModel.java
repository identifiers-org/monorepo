package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiCoveragePayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiInteroperabilityPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiCoverageResponse;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiInteroperabilityResponse;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.helpers.MirIdHelper;
import org.identifiers.cloud.hq.ws.registry.models.helpers.NamespaceHelper;
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
 * <p>
 * Associated model for FairApiController
 */
@Component
public class FairApiModel {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private NamespaceRepository namespaceRepository;
    @Autowired
    private NamespaceHelper namespaceHelper;

    /**
     * Check whether the given LUI (Locally Unique Identifier) is supported, i.e. corresponds to an existing minted ID.
     * In the case of identifiers.org registry, we would be talking about MIR IDs, so this method will tell the
     * requester whether the given ID has been minted and it's in use.
     * @param payload That contains the MIR ID to check (LUI)
     * @return different HTTP Status codes according to the FAIR API specification at https://fairapi.org
     */
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

    /**
     * Given a namespace and a LUI, get their compact identifier version.
     * @param payload Request payload that contains the given namespace and LUI
     * @return the compact identifier if possible, and an adecuate HTTP Response Code
     */
    public FairApiInteroperabilityResponse getCompactIdentifier(FairApiInteroperabilityPayload payload) {
        FairApiInteroperabilityResponse response = new FairApiInteroperabilityResponse();
        if ((payload.getNamespace() != null) && (payload.getLui() != null)) {
            Namespace foundNamespace = namespaceRepository.findByPrefix(payload.getNamespace());
            if ( foundNamespace != null) {
                String compactIdentifier = NamespaceHelper.getCompactIdentifier(foundNamespace, payload.getLui());
                if (compactIdentifier != null) {
                    response.setResponse(compactIdentifier);
                } else {
                    response.setHttpStatus(HttpStatus.BAD_REQUEST);
                    response.setAdditionalInformation(String.format("INVALID LUI '%s' for namespace '%s'", payload.getLui(), payload.getNamespace()));
                }
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setAdditionalInformation(String.format("INVALID Namespace '%s'", payload.getNamespace()));
            }
        } else {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setAdditionalInformation("Both 'namespace' and 'LUI' information are required");
        }
        return response;
    }

    public FairApiInteroperabilityResponse getInteroperabilityUrl(FairApiInteroperabilityPayload payload) {
        // TODO
        FairApiInteroperabilityResponse response = new FairApiInteroperabilityResponse();
        if ((payload.getNamespace() != null) && (payload.getLui() != null)) {
            Namespace foundNamespace = namespaceRepository.findByPrefix(payload.getNamespace());
            if ( foundNamespace != null) {
                String interoperabilityUrl = namespaceHelper.getInteroperabilityUrl(foundNamespace, payload.getLui());
                if (interoperabilityUrl != null) {
                    response.setResponse(interoperabilityUrl);
                } else {
                    response.setHttpStatus(HttpStatus.BAD_REQUEST);
                    response.setAdditionalInformation(String.format("INVALID LUI '%s' for namespace '%s'", payload.getLui(), payload.getNamespace()));
                }
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setAdditionalInformation(String.format("INVALID Namespace '%s'", payload.getNamespace()));
            }
        } else {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setAdditionalInformation("Both 'namespace' and 'LUI' information are required");
        }
        return response;
    }
}
