package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestReactivateResource;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResource;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourceSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourceValidate;
import org.identifiers.cloud.hq.ws.registry.api.responses.*;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationRequestRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationSessionRepository;
import org.identifiers.cloud.hq.ws.registry.models.ResourceLifecycleManagementContext;
import org.identifiers.cloud.hq.ws.registry.models.ResourceLifecycleManagementService;
import org.identifiers.cloud.hq.ws.registry.models.ResourceRegistrationRequestManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.identifiers.cloud.hq.ws.registry.models.validators.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-07-25 12:17
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Main model for resource management api controller
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceManagementApiModel {
    final Map<String, RegistrationValidationChain> registrationValidationChains;

    // Auth Helper
    private final AuthHelper authHelper;

    // Repositories
    private final ResourceRegistrationSessionRepository resourceRegistrationSessionRepository;

    // Services
    private final ResourceRegistrationRequestManagementService resourceRegistrationRequestManagementService;
    private final ResourceLifecycleManagementService resourceLifecycleManagementService;

    // --- Helpers ---
    /**
     * Initialize a response with the default values and the given payload.
     * @param response response to initialize
     * @param payload payload to set in the response
     * @param <T> the type of payload
     */
    private <T> void initDefaultResponse(ServiceResponse<T> response, T payload) {
        response.setApiVersion(ApiCentral.apiVersion)
                .setHttpStatus(HttpStatus.OK);
        response.setPayload(payload);
    }

    private ServiceResponseRegisterResource createRegisterResourceDefaultResponse() {
        ServiceResponseRegisterResource response = new ServiceResponseRegisterResource();
        initDefaultResponse(response, new ServiceResponseRegisterResourcePayload());
        return response;
    }

    private ServiceResponseRegisterResourceSessionEvent createRegisterResourceSessionEventDefaultResponse() {
        ServiceResponseRegisterResourceSessionEvent response = new ServiceResponseRegisterResourceSessionEvent();
        initDefaultResponse(response, new ServiceResponseRegisterResourceSessionEventPayload());
        return response;
    }

    private ServiceResponseDeactivateResource createResourceDeactivationDefaultResponse() {
        ServiceResponseDeactivateResource response = new ServiceResponseDeactivateResource();
        initDefaultResponse(response, new ServiceResponseDeactivateResourcePayload());
        return response;
    }

    private ServiceResponseReactivateResource createResourceReactivationDefaultResponse() {
        ServiceResponseReactivateResource response = new ServiceResponseReactivateResource();
        initDefaultResponse(response, new ServiceResponseReactivateResourcePayload());
        return response;
    }


    private String getAdditionalInformationFrom(ServiceRequestRegisterResourceSessionEvent request) {
        if (request.getPayload().getAdditionalInformation() != null) {
            return request.getPayload().getAdditionalInformation();
        }
        return "No additional information specified";
    }

    private String getCommentFrom(ServiceRequestRegisterResourceSessionEvent request) {
        if (request.getPayload().getComment() != null) {
            return request.getPayload().getComment();
        }
        return "No comment provided";
    }

    private String getRejectionReason(ServiceRequestRegisterResourceSessionEvent request) {
        if (request.getPayload().getRejectionReason() != null) {
            return request.getPayload().getRejectionReason();
        }
        return "No rejection reason provided";
    }

    private String getAcceptanceReason(ServiceRequestRegisterResourceSessionEvent request) {
        if (request.getPayload().getAcceptanceReason() != null) {
            return request.getPayload().getAcceptanceReason();
        }
        return "No acceptance reason provided";
    }

    private ResourceRegistrationSession getResourceRegistrationSession(String eventName, long sessionId,
                                                                       ServiceRequestRegisterResourceSessionEvent request,
                                                                       ServiceResponseRegisterResourceSessionEvent response) {
        Optional<ResourceRegistrationSession> resourceRegistrationSession = resourceRegistrationSessionRepository.findById(sessionId);
        if (resourceRegistrationSession.isEmpty()) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            String errorMessage = String.format("INVALID Resource Registration '%s' request, session with ID '%d' IS NOT VALID", eventName, sessionId);
            response.setErrorMessage(errorMessage);
            log.error(errorMessage);
            return null;
        }
        return resourceRegistrationSession.get();
    }

    // --- API ---
    // Resource Registration API
    public ServiceResponseRegisterResource registerResource(ServiceRequestRegisterResource request) {
        ServiceResponseRegisterResource response = createRegisterResourceDefaultResponse();

        var errors = this.registrationValidationChains.values().stream()
                .map(chain -> chain.validate(request.getPayload()))
                .filter(Optional::isPresent).toList();
        if (errors.isEmpty()) {
            // Translate the data model
            ResourceRegistrationRequest resourceRegistrationRequest =
                    ApiAndDataModelsHelper.getResourceRegistrationRequestFrom(request.getPayload());

            String additionalInformation = "Open API for resource registration request submission";
            String actor = authHelper.getCurrentUsername();
            resourceRegistrationRequestManagementService
                    .startRequest(resourceRegistrationRequest, actor, additionalInformation);
        } else {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            String resourceName = "--- Resource Name NOT SPECIFIED ---";
            if (request.getPayload() != null && request.getPayload().getProviderName() != null) {
                resourceName = request.getPayload().getProviderName();
            }
            String joinedErrors = errors.stream().map(Optional::get).collect(Collectors.joining("\n"));
            String errorMessage = String.format(
                    "INVALID Resource registration request for resource name '%s', due to '%s'",
                    resourceName, joinedErrors);
            response.setErrorMessage(errorMessage);
            log.error(errorMessage);
        }
        return response;
    }

    public ServiceResponseRegisterResourceSessionEvent amendResourceRegistrationRequest(
            long sessionId, ServiceRequestRegisterResourceSessionEvent request) {
        // Default response
        ServiceResponseRegisterResourceSessionEvent response = createRegisterResourceSessionEventDefaultResponse();
        // TODO We need to get the actor from Spring Security
        String actor = authHelper.getCurrentUsername();
        // Locate the resource registration request session
        ResourceRegistrationSession resourceRegistrationSession = getResourceRegistrationSession("AMEND", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Transform the model
            ResourceRegistrationRequest amendedResourceRegistrationRequest = ApiAndDataModelsHelper.getResourceRegistrationRequestFrom(request.getPayload().getResourceRegistrationRequest());
            // Delegate on the resource registration request manager service
            resourceRegistrationRequestManagementService.amendRequest(resourceRegistrationSession, amendedResourceRegistrationRequest, actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterResourceSessionEvent commentResourceRegistrationRequest(long sessionId, ServiceRequestRegisterResourceSessionEvent request) {
        // Default response
        ServiceResponseRegisterResourceSessionEvent response = createRegisterResourceSessionEventDefaultResponse();
        // TODO We need to get the actor from Spring Security
        String actor = authHelper.getCurrentUsername();
        // Locate the resource registration request session
        ResourceRegistrationSession resourceRegistrationSession = getResourceRegistrationSession("COMMENT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the resource registration request manager service
            resourceRegistrationRequestManagementService.commentRequest(resourceRegistrationSession, getCommentFrom(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterResourceSessionEvent rejectResourceRegistrationRequest(long sessionId, ServiceRequestRegisterResourceSessionEvent request) {
        // Default response
        ServiceResponseRegisterResourceSessionEvent response = createRegisterResourceSessionEventDefaultResponse();
        // TODO We need to get the actor from Spring Security
        String actor = authHelper.getCurrentUsername();
        // Locate the resource registration request session
        ResourceRegistrationSession resourceRegistrationSession = getResourceRegistrationSession("REJECT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the resource registration request manager service
            resourceRegistrationRequestManagementService.rejectRequest(resourceRegistrationSession, getRejectionReason(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    public ServiceResponseRegisterResourceSessionEvent acceptResourceRegistrationRequest(long sessionId, ServiceRequestRegisterResourceSessionEvent request) {
        // Default response
        ServiceResponseRegisterResourceSessionEvent response = createRegisterResourceSessionEventDefaultResponse();
        // TODO We need to get the actor from Spring Security
        String actor = authHelper.getCurrentUsername();
        // Locate the resource registration request session
        ResourceRegistrationSession resourceRegistrationSession = getResourceRegistrationSession("ACCEPT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the resource registration request manager service
            resourceRegistrationRequestManagementService.acceptRequest(resourceRegistrationSession, getAcceptanceReason(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    private ServiceResponseRegisterResourceValidate doValidation(ServiceRequestRegisterResourceValidate request,
                                                                 String valueName) {
        ServiceResponseRegisterResourceValidate response = new ServiceResponseRegisterResourceValidate();
        initDefaultResponse(response, new ServiceResponseRegisterResourceValidatePayload());
        // Validate the request

        var payload = request.getPayload();
        Optional<String> error = registrationValidationChains.get(valueName).validate(payload);
        if (error.isPresent()){
            response.setErrorMessage(error.get());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.getPayload().setComment("VALIDATION FAILED");
        } else {
            response.getPayload().setComment("VALIDATION OK");
        }
        return response;
    }

    // Validation API
    public ServiceResponseRegisterResourceValidate validateProviderHomeUrl(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerHomeUrl");
    }

    public ServiceResponseRegisterResourceValidate validateProviderName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerName");
    }

    public ServiceResponseRegisterResourceValidate validateProviderDescription(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerDescription");
    }

    public ServiceResponseRegisterResourceValidate validateProviderLocation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerLocation");
    }

    public ServiceResponseRegisterResourceValidate validateProviderCode(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerCode");
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "institutionName");
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionHomeUrl(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "institutionHomeUrl");
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionDescription(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "institutionDescription");
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionLocation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "institutionLocation");
    }

    public ServiceResponseRegisterResourceValidate validateProviderUrlPattern(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "providerUrlPattern");
    }

    public ServiceResponseRegisterResourceValidate validateSampleId(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "sampleId");
    }

    public ServiceResponseRegisterResourceValidate validateAdditionalInformation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "additionalInformation");
    }

    public ServiceResponseRegisterResourceValidate validateRequester(ServiceRequestRegisterResourceValidate request) {
        var ret = doValidation(request, "requesterName");
        if (HttpStatus.BAD_REQUEST.equals(ret.getHttpStatus())) {
            return ret;
        } else {
            return doValidation(request, "requesterEmail");
        }
    }

    public ServiceResponseRegisterResourceValidate validateRequesterName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "requesterName");
    }

    public ServiceResponseRegisterResourceValidate validateRequesterEmail(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "requesterEmail");
    }

    public ServiceResponseRegisterResourceValidate validateNamespacePrefix(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "prefix");
    }

    public ServiceResponseRegisterResourceValidate validateAuthHelpDescription(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "authHelpDescription");
    }

    public ServiceResponseRegisterResourceValidate validateAuthHelpUrl(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, "authHelpUrl");
    }

    // TODO --- Resource Lifecycle Management API
    // Helper
    private void processResourceLifecycleManagementOperationReport(ServiceResponse<?> response,
                                                                   ResourceLifecycleManagementOperationReport report) {
        if (report.isError()) {
            if (report.getResource() == null) {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            } else {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
            response.setErrorMessage(report.getErrorMessage());
        }
    }
    public ServiceResponseDeactivateResource deactivateResource(long resourceId) {
        ServiceResponseDeactivateResource response = createResourceDeactivationDefaultResponse();
        // TODO Get this information from Spring Security
        String actor = authHelper.getCurrentUsername();
        String additionalInformation = "--- no additional information specified ---";
        ResourceLifecycleManagementOperationReport deactivationOperationReport =
                resourceLifecycleManagementService.deactivateResource(resourceId,
                        resourceLifecycleManagementService.createEmptyContext(),
                        actor,
                        additionalInformation);
        // Let's see what we got back
        processResourceLifecycleManagementOperationReport(response, deactivationOperationReport);
        response.getPayload().setComment(deactivationOperationReport.getAdditionalInformation());
        return response;
    }

    public ServiceResponseReactivateResource reactivateResource(long resourceId, ServiceRequestReactivateResource request) {
        ServiceResponseReactivateResource response = createResourceReactivationDefaultResponse();
        // TODO Get this from Spring Security
        String actor = authHelper.getCurrentUsername();
        String additionalInformation = "--- no additional information specified ---";
        // In the future, I may need a data model transformation helper
        ResourceLifecycleManagementContext context = resourceLifecycleManagementService.createEmptyContext().setResourceReactivationUrlPattern(request.getPayload().getProviderUrlPattern());
        ResourceLifecycleManagementOperationReport activationReport = resourceLifecycleManagementService.reactivateResource(resourceId, context, actor, additionalInformation);
        // Let's see what we got back
        processResourceLifecycleManagementOperationReport(response, activationReport);
        response.getPayload().setComment(activationReport.getAdditionalInformation());
        return response;
    }
}
