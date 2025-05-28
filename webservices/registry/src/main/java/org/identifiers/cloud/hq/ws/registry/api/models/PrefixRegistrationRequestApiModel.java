package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixSessionEventPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseRegisterPrefixSessionEventPayload;
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
    private String getAdditionalInformationFrom(ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        if (request.getPayload().getAdditionalInformation() != null) {
            return request.getPayload().getAdditionalInformation();
        }
        return "No additional information specified";
    }

    private String getCommentFrom(ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        if (request.getPayload().getComment() != null) {
            return request.getPayload().getComment();
        }
        return "No comment provided";
    }

    private String getRejectionReasonFrom(ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        if (request.getPayload().getRejectionReason() != null) {
            return request.getPayload().getRejectionReason();
        }
        return "No rejection reason provided";
    }

    private String getAcceptanceReasonFrom(ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        if (request.getPayload().getAcceptanceReason() != null) {
            return request.getPayload().getAcceptanceReason();
        }
        return "No acceptance reason provided";
    }

    private PrefixRegistrationSession getPrefixRegistrationSession(String eventName, long sessionId,
                                                                   ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request,
                                                                   ServiceResponse<ServiceResponseRegisterPrefixSessionEventPayload> response) {
        Optional<PrefixRegistrationSession> prefixRegistrationSession = prefixRegistrationSessionRepository.findById(sessionId);
        if (prefixRegistrationSession.isEmpty()) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setErrorMessage(String.format("INVALID Prefix Registration %s Request, session with ID '%d' IS NOT VALID", eventName, sessionId));
            log.error("INVALID {} request on NON-EXISTING prefix registration session, with ID '{}'", eventName, sessionId);
            return null;
        }
        return prefixRegistrationSession.get();
    }
    // END - Helpers

    // --- API ---
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> registerPrefix(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixPayload());

        var errorList = registrationValidationChains.values().stream()
                .map(chain -> chain.validate(request.getPayload()))
                .filter(Optional::isPresent).map(Optional::get).toList();

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

            var joinedMessages = String.join("\n", errorList);
            response.setErrorMessage(String.format("INVALID Prefix registration request due to '%s'", joinedMessages));

            String requestedPrefix = "--- NO PREFIX SPECIFIED ---";
            if (request.getPayload() != null && request.getPayload().getRequestedPrefix() != null) {
                requestedPrefix = request.getPayload().getRequestedPrefix();
            }

            log.error("INVALID Prefix registration request for prefix '{}', due to '{}'", requestedPrefix, joinedMessages);
        }
        return response;
    }

    public ServiceResponse<ServiceResponseRegisterPrefixSessionEventPayload> amendPrefixRegistrationRequest(
            long sessionId, ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixSessionEventPayload());
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

    public ServiceResponse<ServiceResponseRegisterPrefixSessionEventPayload> commentPrefixRegistrationRequest(
            long sessionId, ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixSessionEventPayload());
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

    public ServiceResponse<ServiceResponseRegisterPrefixSessionEventPayload> rejectPrefixRegistrationRequest(
            long sessionId, ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixSessionEventPayload());
        String actor = authHelper.getCurrentUsername();
        // Locate the prefix registration request session
        PrefixRegistrationSession prefixRegistrationSession = getPrefixRegistrationSession("REJECT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the prefix registration request management service
            prefixRegistrationRequestManagementService.rejectRequest(prefixRegistrationSession, getRejectionReasonFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponse<ServiceResponseRegisterPrefixSessionEventPayload> acceptPrefixRegistrationRequest(
            long sessionId, ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixSessionEventPayload());
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
