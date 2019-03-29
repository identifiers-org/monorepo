package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEventPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PrefixRegistrationSessionRepository;
import org.identifiers.cloud.hq.ws.registry.models.PrefixRegistrationRequestManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.ApiDataModelHelper;
import org.identifiers.cloud.hq.ws.registry.models.validators.PrefixRegistrationRequestValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-03-28 15:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class PrefixRegistrationRequestApiModel {
    // TODO

    // Prefix registration request validator
    @Autowired
    private PrefixRegistrationRequestValidatorStrategy validatorStrategy;

    // Prefix Registration Request Management Service
    @Autowired
    private PrefixRegistrationRequestManagementService prefixRegistrationRequestManagementService;

    // Repositories
    @Autowired
    private PrefixRegistrationSessionRepository prefixRegistrationSessionRepository;

    // Helpers
    private ServiceResponseRegisterPrefix createRegisterPrefixDefaultResponse() {
        ServiceResponseRegisterPrefix response = new ServiceResponseRegisterPrefix();
        response.setApiVersion(ApiCentral.apiVersion).setHttpStatus(HttpStatus.OK);
        response.setPayload(new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    private ServiceResponseRegisterPrefixSessionEvent createRegisterPrefixSessionEventDefaultResponse() {
        ServiceResponseRegisterPrefixSessionEvent response = new ServiceResponseRegisterPrefixSessionEvent();
        response.setApiVersion(ApiCentral.apiVersion).setHttpStatus(HttpStatus.OK);
        response.setPayload(new ServiceResponseRegisterPrefixSessionEventPayload());
        return response;
    }

    private String getAdditionalInformationFrom(ServiceRequestRegisterPrefixSessionEvent request) {
        if (request.getPayload().getAdditionalInformation() != null) {
            return request.getPayload().getAdditionalInformation();
        }
        return "No additional information specified";
    }
    // END - Helpers

    // --- API ---
    public ServiceResponseRegisterPrefix registerPrefix(ServiceRequestRegisterPrefix request) {
        // Create default response
        ServiceResponseRegisterPrefix response = createRegisterPrefixDefaultResponse();
        boolean isValid = false;
        // Determine who is the actor of this
        String actor = "ANONYMOUS";
        // Possible additional information
        String additionalInformation = "Source, Open API for prefix registration request submission";
        // Run request validation
        try {
            isValid = validatorStrategy.validate(request.getPayload());
        } catch (RuntimeException e) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setErrorMessage(String.format("INVALID Prefix registration request due to '%s'", e.getMessage()));
            String requestedPrefix = "--- NO PREFIX SPECIFIED ---";
            if ((request.getPayload() != null) && (request.getPayload().getRequestedPrefix() != null)) {
                requestedPrefix = request.getPayload().getRequestedPrefix();
            }
            log.error(String.format("INVALID Prefix registration request for prefix '%s', due to '%s'", requestedPrefix, e.getMessage()));
        }
        if (isValid) {
            // Translate data model
            PrefixRegistrationRequest prefixRegistrationRequest =
                    ApiDataModelHelper.getPrefixRegistrationRequest(request.getPayload());
            // Delegate on the Prefix Registration Request Management Service
            prefixRegistrationRequestManagementService.startRequest(prefixRegistrationRequest,
                    actor,
                    additionalInformation);
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent amendPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO Actor unknnown right now, until we get Spring Security
        String actor = "UNKNOWN";
        String additionalInformation = "No additional information specified";
        if (request.getPayload().getAdditionalInformation() != null) {
            additionalInformation = request.getPayload().getAdditionalInformation();
        }
        // Locate the prefix registration request session
        Optional<PrefixRegistrationSession> prefixRegistrationSession = prefixRegistrationSessionRepository.findById(sessionId);
        if (!prefixRegistrationSession.isPresent()) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setErrorMessage(String.format("INVALID Prefix Registration Amend Request, session with ID '%d' IS NOT VALID", sessionId));
            log.error(String.format("INVALID AMEND request on NON-EXISTING prefix registration session, with ID '%d'", sessionId));
        } else {
            // Transform the model
            PrefixRegistrationRequest prefixRegistrationRequest = ApiDataModelHelper.getPrefixRegistrationRequest(request.getPayload().getPrefixRegistrationRequest());
            // Delegate on the Prefix Registration Request Management Service
            prefixRegistrationRequestManagementService.amendRequest(prefixRegistrationSession.get(), prefixRegistrationRequest, actor, additionalInformation);
        }
        return response;
    }

    // TODO - Comment on prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent commentPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }

    // TODO - Reject prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent rejectPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }

    // TODO - Accept prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent acceptPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }
}
