package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixValidate;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixValidateRequest;
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

    private ServiceResponseRegisterPrefixValidateRequest doValidation(ServiceRequestRegisterPrefixValidate request,
                                                                      String fieldName) {
        ServiceResponseRegisterPrefixValidateRequest response = new ServiceResponseRegisterPrefixValidateRequest();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        // Validate the request
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
    public ServiceResponseRegisterPrefixValidateRequest validateName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "namespaceName");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "namespaceDescription");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderHomeUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerHomeUrl");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerName");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerDescription");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderLocation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerLocation");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderCode(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerCode");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "institutionName");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "institutionDescription");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionLocation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "institutionLocation");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequestedPrefix(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "prefix");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderUrlPattern(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "providerUrlPattern");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateSampleId(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "sampleId");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateIdRegexPattern(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "idRegexPattern");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateReferences(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "supportingReferences");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAdditionalInformation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "additionalInformation");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequester(ServiceRequestRegisterPrefixValidate request) {
        var ret = doValidation(request, "requesterName");
        if (HttpStatus.BAD_REQUEST.equals(ret.getHttpStatus())) {
            return ret;
        } else {
            return doValidation(request, "requesterEmail");
        }
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionHomeUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "institutionHomeUrl");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequesterName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "requesterName");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequesterEmail(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "requesterEmail");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAuthHelpDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "authHelpDescription");
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAuthHelpUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, "authHelpUrl");
    }
}
