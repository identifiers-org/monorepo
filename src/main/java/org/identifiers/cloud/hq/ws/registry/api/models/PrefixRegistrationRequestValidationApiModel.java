package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixValidate;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixValidateRequest;
import org.identifiers.cloud.hq.ws.registry.models.validators.PrefixRegistrationRequestValidator;
import org.identifiers.cloud.hq.ws.registry.models.validators.PrefixRegistrationRequestValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-03-18 13:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class PrefixRegistrationRequestValidationApiModel {
    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorRequestedPrefix")
    private PrefixRegistrationRequestValidator prefixValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorName")
    private PrefixRegistrationRequestValidator nameValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorDescription")
    private PrefixRegistrationRequestValidator descriptionValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorProviderHomeUrl")
    private PrefixRegistrationRequestValidator providerHomeUrlValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorProviderName")
    private PrefixRegistrationRequestValidator providerNameValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorProviderDescription")
    private PrefixRegistrationRequestValidator providerDescriptionValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorProviderLocation")
    private PrefixRegistrationRequestValidator providerLocationValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorProviderCode")
    private PrefixRegistrationRequestValidator providerCodeValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorInstitutionName")
    private PrefixRegistrationRequestValidator institutionNameValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorInstitutionHomeUrl")
    private PrefixRegistrationRequestValidator institutionHomeUrlValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorInstitutionDescription")
    private PrefixRegistrationRequestValidator institutionDescriptionValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorInstitutionLocation")
    private PrefixRegistrationRequestValidator institutionLocationValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorProviderUrlPattern")
    private PrefixRegistrationRequestValidator providerUrlPatternValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
    private PrefixRegistrationRequestValidator crossedSampleIdProviderUrlPatternValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId")
    private PrefixRegistrationRequestValidator crossedIdRegexPatternAndSampleIdValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorReferences")
    private PrefixRegistrationRequestValidator referencesValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorAdditionalInformation")
    private PrefixRegistrationRequestValidator additionalInformationValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorRequester")
    private PrefixRegistrationRequestValidator requesterValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorRequesterName")
    private PrefixRegistrationRequestValidator requesterNameValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorRequesterEmail")
    private PrefixRegistrationRequestValidator requesterEmailValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorAuthHelpDescription")
    private PrefixRegistrationRequestValidator authHelpDescriptionValidator;

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorAuthHelpUrl")
    private PrefixRegistrationRequestValidator authHelpUrlValidator;

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
                                                                      PrefixRegistrationRequestValidator validator) {
        // TODO - Check API version information?
        ServiceResponseRegisterPrefixValidateRequest response = new ServiceResponseRegisterPrefixValidateRequest();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        // Validate the request
        boolean isValidRequest = false;
        try {
            isValidRequest = validator.validate(request.getPayload());
        } catch (PrefixRegistrationRequestValidatorException e) {
            response.setErrorMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.getPayload().setComment("VALIDATION FAILED");
        }
        if (isValidRequest) {
            response.getPayload().setComment("VALIDATION OK");
        }
        return response;
    }

    // -- API --
    public ServiceResponseRegisterPrefixValidateRequest validateName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, nameValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, descriptionValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderHomeUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerHomeUrlValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerNameValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerDescriptionValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderLocation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerLocationValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderCode(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerCodeValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, institutionNameValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, institutionDescriptionValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionLocation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, institutionLocationValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequestedPrefix(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, prefixValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateProviderUrlPattern(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, providerUrlPatternValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateSampleId(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, crossedSampleIdProviderUrlPatternValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateIdRegexPattern(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, crossedIdRegexPatternAndSampleIdValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateReferences(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, referencesValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAdditionalInformation(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, additionalInformationValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequester(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, requesterValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateInstitutionHomeUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, institutionHomeUrlValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequesterName(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, requesterNameValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateRequesterEmail(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, requesterEmailValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAuthHelpDescription(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, authHelpDescriptionValidator);
    }

    public ServiceResponseRegisterPrefixValidateRequest validateAuthHelpUrl(ServiceRequestRegisterPrefixValidate request) {
        return doValidation(request, authHelpUrlValidator);
    }
}
