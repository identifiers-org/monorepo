package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorProviderCode")
public class PrefixRegistrationRequestValidatorProviderCode implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // Provider code must be provided
        if (request.getProviderCode() == null) {
            throw new PrefixRegistrationRequestValidatorException("Provider Code is REQUIRED, but it's missing");
        }
        // As this is the first provider within the requested prefix, we don't need to check that the provider code is unique.
        return true;
    }
}
