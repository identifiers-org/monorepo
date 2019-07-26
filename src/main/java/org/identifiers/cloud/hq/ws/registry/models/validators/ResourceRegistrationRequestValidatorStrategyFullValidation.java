package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 17:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class ResourceRegistrationRequestValidatorStrategyFullValidation implements ResourceRegistrationRequestValidatorStrategy {
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

    @Override
    public List<ResourceRegistrationRequestValidator> getValidationChain() {
        return Arrays.asList(
                providerHomeUrlValidator,
                providerNameValidator,
                providerDescriptionValidator,
                providerLocationValidator,
                providerCodeValidator,
                institutionNameValidator,
                institutionHomeUrlValidator,
                institutionDescriptionValidator,
                institutionLocationValidator,
                providerUrlPatternValidator,
                crossedSampleIdProviderUrlPatternValidator,
                additionalInformationValidator,
                requesterValidator
        );
    }
}
