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
@Qualifier("prefixRegistrationRequestValidatorCrossedExampleIdentifierResourceAccessRule")
public class PrefixRegistrationRequestValidatorCrossedExampleIdentifierResourceAccessRule implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        List<String> errors = new ArrayList<>();
        // Check resource access rule
        try {
            new PrefixRegistrationRequestValidatorResourceAccessRule().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // Check example identifier
        try {
            new PrefixRegistrationRequestValidatorExampleIdentifier().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // If any error, report back
        if (!errors.isEmpty()) {
            throw new PrefixRegistrationRequestValidatorException(String.join("\n", errors));
        }
        // Cross-validate example identifier
        try {
            WebPageCheckerFactory.getWebPageChecker().checkWebPageUrl(ResourceAccessHelper.getResourceUrlFor(request.getResourceAccessRule(), request.getExampleIdentifier()));
        } catch (WebPageCheckerException e) {
            throw new PrefixRegistrationRequestValidatorException(
                    String.format("The provided Resource Access Rule '%s' " +
                                    "combined with the provided Example Identifier '%s' " +
                                    "into '%s' DOES NOT VALIDATE", request.getResourceAccessRule(),
                            request.getExampleIdentifier(),
                            ResourceAccessHelper.getResourceUrlFor(request.getResourceAccessRule(),
                                    request.getExampleIdentifier())));
        }
        return true;
    }
}