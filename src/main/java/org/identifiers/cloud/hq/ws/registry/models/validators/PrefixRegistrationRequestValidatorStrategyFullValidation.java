package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
public class PrefixRegistrationRequestValidatorStrategyFullValidation implements PrefixRegistrationRequestValidatorStrategy {

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorRequestedPrefix")
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
    @Qualifier("prefixRegistrationRequestValidatorOrganization")
    private PrefixRegistrationRequestValidator organizationValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorProviderUrlPattern")
    private PrefixRegistrationRequestValidator providerUrlPatternValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
    private PrefixRegistrationRequestValidator crossedSampleIdProviderUrlPattern;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId")
    private PrefixRegistrationRequestValidator crossedRegexPatternAndExampleIdentifierValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorReferences")
    private PrefixRegistrationRequestValidator referencesValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorAdditionalInformation")
    private PrefixRegistrationRequestValidator additionalInformationValidator;

    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorRequester")
    private PrefixRegistrationRequestValidator requesterValidator;

    @Override
    public List<PrefixRegistrationRequestValidator> getValidationChain() {
        return Arrays.asList(
                nameValidator,
                descriptionValidator,
                providerHomeUrlValidator,
                organizationValidator,
                prefixValidator,
                providerUrlPatternValidator,
                crossedSampleIdProviderUrlPattern,
                crossedRegexPatternAndExampleIdentifierValidator,
                referencesValidator,
                additionalInformationValidator,
                requesterValidator
        );
    }
}
