package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationValidationChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-03-18 13:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@RequiredArgsConstructor
public class PrefixRegistrationRequestValidationApiModel {
    final Map<String, RegistrationValidationChain> registrationValidationChains;

    // -- Helpers --
    private ServiceResponse<ServiceResponseRegisterPrefixPayload> doValidation(ServiceRequest<ServiceRequestRegisterPrefixPayload> request,
                                                                               String fieldName) {
        var response = ServiceResponse.of(new ServiceResponseRegisterPrefixPayload());
        // Validate the request
        if (request.getPayload() == null) {
            response.setErrorMessage("Payload is required");
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setPayload(null);
            return response;
        }

        RegistrationValidationChain validationChain = registrationValidationChains.get(fieldName);
        Optional<String> error = validationChain.validate(request.getPayload());
        if (error.isPresent()) {
            response.setErrorMessage(error.get());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.getPayload().setComment("VALIDATION FAILED");
        } else {
            response.getPayload().setComment("VALIDATION OK");
        }
        return response;
    }



    // -- API --
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateName(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "namespaceName");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateDescription(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "namespaceDescription");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderHomeUrl(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerHomeUrl");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderName(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerName");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderDescription(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerDescription");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderLocation(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerLocation");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderCode(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerCode");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionName(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "institutionName");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionDescription(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "institutionDescription");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionLocation(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "institutionLocation");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequestedPrefix(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "prefix");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderUrlPattern(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "providerUrlPattern");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateSampleId(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "sampleId");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateIdRegexPattern(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "idRegexPattern");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateReferences(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "supportingReferences");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateAdditionalInformation(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "additionalInformation");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequester(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var ret = doValidation(request, "requesterName");
        if (HttpStatus.BAD_REQUEST.equals(ret.getHttpStatus())) {
            return ret;
        } else {
            return doValidation(request, "requesterEmail");
        }
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionHomeUrl(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "institutionHomeUrl");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequesterName(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "requesterName");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequesterEmail(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "requesterEmail");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateAuthHelpDescription(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "authHelpDescription");
    }

    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateAuthHelpUrl(ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        return doValidation(request, "authHelpUrl");
    }
}
