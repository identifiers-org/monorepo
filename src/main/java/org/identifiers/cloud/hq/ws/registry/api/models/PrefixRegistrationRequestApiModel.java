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
import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
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

    private String getCommentFrom(ServiceRequestRegisterPrefixSessionEvent request) {
        if (request.getPayload().getComment() != null) {
            return request.getPayload().getComment();
        }
        return "No comment provided";
    }

    private String getRejectionReasonFrom(ServiceRequestRegisterPrefixSessionEvent request) {
        if (request.getPayload().getRejectionReason() != null) {
            return request.getPayload().getRejectionReason();
        }
        return "No rejection reason provided";
    }

    private String getAcceptanceReasonFrom(ServiceRequestRegisterPrefixSessionEvent request) {
        if (request.getPayload().getAcceptanceReason() != null) {
            return request.getPayload().getAcceptanceReason();
        }
        return "No acceptance reason provided";
    }

    private PrefixRegistrationSession getPrefixRegistrationSession(String eventName, long sessionId, ServiceRequestRegisterPrefixSessionEvent request, ServiceResponseRegisterPrefixSessionEvent response) {
        Optional<PrefixRegistrationSession> prefixRegistrationSession = prefixRegistrationSessionRepository.findById(sessionId);
        if (!prefixRegistrationSession.isPresent()) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setErrorMessage(String.format("INVALID Prefix Registration %s Request, session with ID '%d' IS NOT VALID", eventName, sessionId));
            log.error(String.format("INVALID %s request on NON-EXISTING prefix registration session, with ID '%d'", eventName, sessionId));
            return null;
        }
        return prefixRegistrationSession.get();
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
                    ApiAndDataModelsHelper.getPrefixRegistrationRequest(request.getPayload());
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
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("AMEND", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Transform the model
            PrefixRegistrationRequest prefixRegistrationRequest = ApiAndDataModelsHelper.getPrefixRegistrationRequest(request.getPayload().getPrefixRegistrationRequest());
            // Delegate on the Prefix Registration Request Management Service
            prefixRegistrationRequestManagementService.amendRequest(prefixRegistrationSession,
                    prefixRegistrationRequest,
                    actor,
                    getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent commentPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO Actor unknnown right now, until we get Spring Security
        String actor = "UNKNOWN";
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("COMMENT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // We located the session
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.commentRequest(prefixRegistrationSession, getCommentFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    // TODO - Reject prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent rejectPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO Actor unknnown right now, until we get Spring Security
        String actor = "UNKNOWN";
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("REJECT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.rejectRequest(prefixRegistrationSession, getRejectionReasonFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    // TODO - Accept prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent acceptPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO Actor unknnown right now, until we get Spring Security
        String actor = "UNKNOWN";
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("ACCEPT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.acceptRequest(prefixRegistrationSession, getAcceptanceReasonFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }
}
