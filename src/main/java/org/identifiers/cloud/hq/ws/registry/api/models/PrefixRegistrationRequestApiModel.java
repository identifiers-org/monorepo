package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
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
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationValidationChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-03-28 15:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrefixRegistrationRequestApiModel {
    private final PrefixRegistrationRequestManagementService prefixRegistrationRequestManagementService;
    private final AuthHelper authHelper;
    private final PrefixRegistrationSessionRepository prefixRegistrationSessionRepository;
    private final Map<String, RegistrationValidationChain> registrationValidationChains;

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

    private PrefixRegistrationSession getPrefixRegistrationSession(String eventName, long sessionId,
                                                                   ServiceRequestRegisterPrefixSessionEvent request,
                                                                   ServiceResponseRegisterPrefixSessionEvent response) {
        Optional<PrefixRegistrationSession> prefixRegistrationSession = prefixRegistrationSessionRepository.findById(sessionId);
        if (prefixRegistrationSession.isEmpty()) {
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
        ServiceResponseRegisterPrefix response = createRegisterPrefixDefaultResponse();

        var errorList = registrationValidationChains.values().stream()
                .map(chain -> chain.validate(request.getPayload()))
                .filter(Optional::isPresent).toList();

        if (errorList.isEmpty()) {
            // Translate data model
            PrefixRegistrationRequest prefixRegistrationRequest =
                    ApiAndDataModelsHelper.getPrefixRegistrationRequest(request.getPayload());

            String actor = authHelper.getCurrentUsername();
            String additionalInformation = "Source, Open API for prefix registration request submission";
            prefixRegistrationRequestManagementService.startRequest(
                    prefixRegistrationRequest, actor, additionalInformation);
        } else {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);

            var joinedMessages = errorList.stream().map(Optional::get).collect(Collectors.joining("\n"));
            response.setErrorMessage(String.format("INVALID Prefix registration request due to '%s'", joinedMessages));

            String requestedPrefix = "--- NO PREFIX SPECIFIED ---";
            if (request.getPayload() != null && request.getPayload().getRequestedPrefix() != null) {
                requestedPrefix = request.getPayload().getRequestedPrefix();
            }

            log.error(String.format("INVALID Prefix registration request for prefix '%s', due to '%s'", requestedPrefix, joinedMessages));
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent amendPrefixRegistrationRequest(
            long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        String actor = authHelper.getCurrentUsername();
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession =
                getPrefixRegistrationSession("AMEND", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Transform the model
            PrefixRegistrationRequest prefixRegistrationRequest =
                    ApiAndDataModelsHelper.getPrefixRegistrationRequest(request.getPayload().getPrefixRegistrationRequest());
            // Delegate on the Prefix Registration Request Management Service
            prefixRegistrationRequestManagementService.amendRequest(prefixRegistrationSession,
                    prefixRegistrationRequest,
                    actor,
                    getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent commentPrefixRegistrationRequest(
            long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        String actor = authHelper.getCurrentUsername();
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession =
                getPrefixRegistrationSession("COMMENT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // We located the session
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.commentRequest(
                    prefixRegistrationSession, getCommentFrom(request),
                    actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent rejectPrefixRegistrationRequest(
            long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        String actor = authHelper.getCurrentUsername();
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("REJECT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.rejectRequest(prefixRegistrationSession, getRejectionReasonFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterPrefixSessionEvent acceptPrefixRegistrationRequest(
            long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        String actor = authHelper.getCurrentUsername();
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("ACCEPT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.acceptRequest(prefixRegistrationSession, getAcceptanceReasonFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }
}
