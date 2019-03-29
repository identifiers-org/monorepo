package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:58
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
public class PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
        List<String> errors = new ArrayList<>();
        // Check resource access rule
        try {
            new PrefixRegistrationRequestValidatorProviderUrlPattern().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // Check example identifier
        try {
            new PrefixRegistrationRequestValidatorSampleId().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // If any error, report back
        if (!errors.isEmpty()) {
            throw new PrefixRegistrationRequestValidatorException(String.join("\n", errors));
        }
        // Cross-validate example identifier
        try {
            WebPageCheckerFactory.getWebPageChecker().checkWebPageUrl(ResourceAccessHelper.getResourceUrlFor(request.getProviderUrlPattern(), request.getSampleId()));
        } catch (WebPageCheckerException e) {
            throw new PrefixRegistrationRequestValidatorException(
                    String.format("The provided Provider URL Pattern '%s' " +
                                    "combined with the provided Sample ID '%s' " +
                                    "into '%s' DOES NOT VALIDATE", request.getProviderUrlPattern(),
                            request.getSampleId(),
                            ResourceAccessHelper.getResourceUrlFor(request.getProviderUrlPattern(),
                                    request.getSampleId())));
        }
        return true;
    }
}