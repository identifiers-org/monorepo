package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceRegistrationRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Component
public class ResourceManagementApiModel {

    // --- Validators ---
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

    // --- API ---
    
    // TODO
}
