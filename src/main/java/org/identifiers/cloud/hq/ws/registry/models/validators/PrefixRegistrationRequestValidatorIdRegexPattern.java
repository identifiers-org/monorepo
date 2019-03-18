package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorIdRegexPattern")
public class PrefixRegistrationRequestValidatorIdRegexPattern implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getIdRegexPattern() == null) {
            throw new PrefixRegistrationRequestValidatorException("MISSING REQUIRED ID Regex Pattern");
        }
        return true;
    }
}