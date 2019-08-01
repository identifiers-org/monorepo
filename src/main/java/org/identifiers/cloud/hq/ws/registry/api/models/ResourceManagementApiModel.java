package org.identifiers.cloud.hq.ws.registry.api.models;

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
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceRegistrationRequestValidator;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceRegistrationRequestValidatorException;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceRegistrationRequestValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
public class ResourceManagementApiModel {

    // --- Validators ---
    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorNamespacePrefix")
    private ResourceRegistrationRequestValidator namespacePrefixValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderHomeUrl")
    private ResourceRegistrationRequestValidator providerHomeUrlValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderName")
    private ResourceRegistrationRequestValidator providerNameValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderDescription")
    private ResourceRegistrationRequestValidator providerDescriptionValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderLocation")
    private ResourceRegistrationRequestValidator providerLocationValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderCode")
    private ResourceRegistrationRequestValidator providerCodeValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorInstitutionName")
    private ResourceRegistrationRequestValidator institutionNameValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorInstitutionHomeUrl")
    private ResourceRegistrationRequestValidator institutionHomeUrlValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorInstitutionName")
    private ResourceRegistrationRequestValidator institutionDescriptionValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorInstitutionLocation")
    private ResourceRegistrationRequestValidator institutionLocationValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorProviderUrlPattern")
    private ResourceRegistrationRequestValidator providerUrlPatternValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
    private ResourceRegistrationRequestValidator crossedSampleIdProviderUrlPatternValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorAdditionalInformation")
    private ResourceRegistrationRequestValidator additionalInformationValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorRequester")
    private ResourceRegistrationRequestValidator requesterValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorRequesterName")
    private ResourceRegistrationRequestValidator requesterNameValidator;

    @Autowired
    @Qualifier("ResourceRegistrationRequestValidatorRequesterEmail")
    private ResourceRegistrationRequestValidator requesterEmailValidator;

    // Resource Registration Request validation strategy
    @Autowired
    private ResourceRegistrationRequestValidatorStrategy validatorStrategy;

    // Repositories
    @Autowired
    private ResourceRegistrationRequestRepository resourceRegistrationRequestRepository;
    @Autowired
    private ResourceRegistrationSessionRepository resourceRegistrationSessionRepository;

    // Services
    @Autowired
    private ResourceRegistrationRequestManagementService resourceRegistrationRequestManagementService;
    @Autowired
    private ResourceLifecycleManagementService resourceLifecycleManagementService;

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

    private ResourceRegistrationSession getResourceRegistrationSession(String eventName, long sessionId, ServiceRequestRegisterResourceSessionEvent request, ServiceResponseRegisterResourceSessionEvent response) {
        Optional<ResourceRegistrationSession> resourceRegistrationSession = resourceRegistrationSessionRepository.findById(sessionId);
        if (!resourceRegistrationSession.isPresent()) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            String errorMessage = String.format("INVALID Resource Registration '%s' request, session with ID '%d' IS NOT VALID", eventName, sessionId);
            response.setErrorMessage(errorMessage);
            log.error(errorMessage);
            return null;
        }
        return resourceRegistrationSession.get();
    }

    private ServiceResponseRegisterResourceValidate doValidation(ServiceRequestRegisterResourceValidate request,
                                                                 ResourceRegistrationRequestValidator validator) {
        // TODO - Check API version information?
        ServiceResponseRegisterResourceValidate response = new ServiceResponseRegisterResourceValidate();
        initDefaultResponse(response, new ServiceResponseRegisterResourceValidatePayload());
        // Validate the request
        boolean isValidRequest = false;
        try {
            isValidRequest = validator.validate(request.getPayload());
        } catch (ResourceRegistrationRequestValidatorException e) {
            response.setErrorMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.getPayload().setComment("VALIDATION FAILED");
        }
        if (isValidRequest) {
            response.getPayload().setComment("VALIDATION OK");
        }
        return response;
    }

    // --- API ---
    // Resource Registration API
    public ServiceResponseRegisterResource registerResource(ServiceRequestRegisterResource request) {
        // Create default response
        ServiceResponseRegisterResource response = createRegisterResourceDefaultResponse();
        boolean isValid = false;
        // Actor
        String actor = "ANONYMOUS";
        // Additional information
        String additionalInformation = "Open API for resource registration request submission";
        // Run request validation
        try {
            isValid = validatorStrategy.validate(request.getPayload());
        } catch (RuntimeException e) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            String resourceName = "--- Resource Name NOT SPECIFIED ---";
            if ((request.getPayload() != null) && (request.getPayload().getProviderName() != null)) {
                resourceName = request.getPayload().getProviderName();
            }
            String errorMessage = String.format("INVALID Resource registration request for resource name '%s', due to '%s'", resourceName, e.getMessage());
            response.setErrorMessage(errorMessage);
            log.error(errorMessage);
        }
        if (isValid) {
            // Translate the data model
            ResourceRegistrationRequest resourceRegistrationRequest =
                    ApiAndDataModelsHelper.getResourceRegistrationRequestFrom(request.getPayload());
            // Delegate on resource registration request management service
            resourceRegistrationRequestManagementService.startRequest(resourceRegistrationRequest, actor, additionalInformation);
        }
        return response;
    }

    public ServiceResponseRegisterResourceSessionEvent amendResourceRegistrationRequest(long sessionId, ServiceRequestRegisterResourceSessionEvent request) {
        // Default response
        ServiceResponseRegisterResourceSessionEvent response = createRegisterResourceSessionEventDefaultResponse();
        // TODO We need to get the actor from Spring Security
        String actor = "UNKNOWN";
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
        String actor = "UNKNOWN";
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
        String actor = "UNKNOWN";
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
        String actor = "UNKNOWN";
        // Locate the resource registration request session
        ResourceRegistrationSession resourceRegistrationSession = getResourceRegistrationSession("ACCEPT", sessionId, request, response);
        if (response.getHttpStatus() == HttpStatus.OK) {
            // Delegate on the resource registration request manager service
            resourceRegistrationRequestManagementService.acceptRequest(resourceRegistrationSession, getAcceptanceReason(request), actor, getAdditionalInformationFrom(request));
        }
        return response;
    }

    // Validation API
    public ServiceResponseRegisterResourceValidate validateProviderHomeUrl(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerHomeUrlValidator);
    }

    public ServiceResponseRegisterResourceValidate validateProviderName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerNameValidator);
    }

    public ServiceResponseRegisterResourceValidate validateProviderDescription(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerDescriptionValidator);
    }

    public ServiceResponseRegisterResourceValidate validateProviderLocation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerLocationValidator);
    }

    public ServiceResponseRegisterResourceValidate validateProviderCode(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerCodeValidator);
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, institutionNameValidator);
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionHomeUrl(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, institutionHomeUrlValidator);
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionDescription(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, institutionDescriptionValidator);
    }

    public ServiceResponseRegisterResourceValidate validateInstitutionLocation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, institutionLocationValidator);
    }

    public ServiceResponseRegisterResourceValidate validateProviderUrlPattern(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, providerUrlPatternValidator);
    }

    public ServiceResponseRegisterResourceValidate validateSampleId(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, crossedSampleIdProviderUrlPatternValidator);
    }

    public ServiceResponseRegisterResourceValidate validateAdditionalInformation(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, additionalInformationValidator);
    }

    public ServiceResponseRegisterResourceValidate validateRequester(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, requesterValidator);
    }

    public ServiceResponseRegisterResourceValidate validateRequesterName(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, requesterNameValidator);
    }

    public ServiceResponseRegisterResourceValidate validateRequesterEmail(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, requesterEmailValidator);
    }

    public ServiceResponseRegisterResourceValidate validateNamespacePrefix(ServiceRequestRegisterResourceValidate request) {
        return doValidation(request, namespacePrefixValidator);
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
        String actor = "UNKNOWN";
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
        String actor = "UNKNOWN";
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
