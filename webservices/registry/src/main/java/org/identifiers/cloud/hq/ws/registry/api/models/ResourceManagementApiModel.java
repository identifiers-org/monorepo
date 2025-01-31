package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.*;
import org.identifiers.cloud.commons.messages.responses.registry.*;
import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationSessionRepository;
import org.identifiers.cloud.hq.ws.registry.models.ResourceLifecycleManagementContext;
import org.identifiers.cloud.hq.ws.registry.models.ResourceLifecycleManagementService;
import org.identifiers.cloud.hq.ws.registry.models.ResourceRegistrationRequestManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.identifiers.cloud.hq.ws.registry.models.validators.*;
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

    private String getAdditionalInformationFrom(ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        if (request.getPayload().getAdditionalInformation() != null) {
            return request.getPayload().getAdditionalInformation();
        }
        return "No additional information specified";
    }

    private String getCommentFrom(ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        if (request.getPayload().getComment() != null) {
            return request.getPayload().getComment();
        }
        return "No comment provided";
    }

    private String getRejectionReason(ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        if (request.getPayload().getRejectionReason() != null) {
            return request.getPayload().getRejectionReason();
        }
        return "No rejection reason provided";
    }

    private String getAcceptanceReason(ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        if (request.getPayload().getAcceptanceReason() != null) {
            return request.getPayload().getAcceptanceReason();
        }
        return "No acceptance reason provided";
    }

    private ResourceRegistrationSession getResourceRegistrationSession(String eventName, long sessionId,
                                                                       ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request,
                                                                       ServiceResponse<ServiceResponseRegisterResourceSessionEventPayload> response) {
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
    public ServiceResponse<ServiceResponseRegisterResourcePayload> registerResource(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = ServiceResponse.of(new ServiceResponseRegisterResourcePayload());

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

    public ServiceResponse<ServiceResponseRegisterResourceSessionEventPayload> amendResourceRegistrationRequest(
            long sessionId, ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        // Default response
        var response = ServiceResponse.of(new ServiceResponseRegisterResourceSessionEventPayload());

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

    public ServiceResponse<ServiceResponseRegisterResourceSessionEventPayload> commentResourceRegistrationRequest(long sessionId, ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        // Default response
        var response = ServiceResponse.of(new ServiceResponseRegisterResourceSessionEventPayload());

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

    public ServiceResponse<ServiceResponseRegisterResourceSessionEventPayload> rejectResourceRegistrationRequest(long sessionId, ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        // Default response
        var response = ServiceResponse.of(new ServiceResponseRegisterResourceSessionEventPayload());
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

    public ServiceResponse<ServiceResponseRegisterResourceSessionEventPayload> acceptResourceRegistrationRequest(long sessionId, ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        // Default response
        var response = ServiceResponse.of(new ServiceResponseRegisterResourceSessionEventPayload());
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

    private ServiceResponse<ServiceResponseRegisterResourceValidatePayload> doValidation(
                                                        ServiceRequest<ServiceRequestRegisterResourcePayload> request,
                                                        String valueName) {
        var payload = new ServiceResponseRegisterResourceValidatePayload();
        var response = ServiceResponse.of(payload);

        Optional<String> error = registrationValidationChains.get(valueName).validate(request.getPayload());
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
    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderHomeUrl(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerHomeUrl");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderName(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerName");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderDescription(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerDescription");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderLocation(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerLocation");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderCode(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerCode");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateInstitutionName(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "institutionName");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateInstitutionHomeUrl(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "institutionHomeUrl");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateInstitutionDescription(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "institutionDescription");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateInstitutionLocation(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "institutionLocation");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateProviderUrlPattern(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "providerUrlPattern");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateSampleId(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "sampleId");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateAdditionalInformation(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "additionalInformation");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateRequester(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var ret = doValidation(request, "requesterName");
        if (HttpStatus.BAD_REQUEST.equals(ret.getHttpStatus())) {
            return ret;
        } else {
            return doValidation(request, "requesterEmail");
        }
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateRequesterName(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "requesterName");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateRequesterEmail(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "requesterEmail");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateNamespacePrefix(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "prefix");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateAuthHelpDescription(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        return doValidation(request, "authHelpDescription");
    }

    public ServiceResponse<ServiceResponseRegisterResourceValidatePayload> validateAuthHelpUrl(ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
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
    public ServiceResponse<ServiceResponseDeactivateResourcePayload> deactivateResource(long resourceId) {
        var response = ServiceResponse.of(new ServiceResponseDeactivateResourcePayload());
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

    public ServiceResponse<ServiceResponseReactivateResourcePayload> reactivateResource(long resourceId,
                                                                                        ServiceRequest<ServiceRequestReactivateResourcePayload> request) {
        var response = ServiceResponse.of(new ServiceResponseReactivateResourcePayload());
        // TODO Get this from Spring Security
        String actor = authHelper.getCurrentUsername();
        String additionalInformation = "--- no additional information specified ---";
        // In the future, I may need a data model transformation helper
        ResourceLifecycleManagementContext context = resourceLifecycleManagementService
            .createEmptyContext().setResourceReactivationUrlPattern(request.getPayload().getProviderUrlPattern());
        ResourceLifecycleManagementOperationReport activationReport = resourceLifecycleManagementService
                                            .reactivateResource(resourceId, context, actor, additionalInformation);
        // Let's see what we got back
        processResourceLifecycleManagementOperationReport(response, activationReport);
        response.getPayload().setComment(activationReport.getAdditionalInformation());
        return response;
    }
}
