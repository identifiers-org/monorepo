package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
        if (request.getIdRegexPattern() == null || request.getIdRegexPattern().trim().isEmpty()) {
            throw new PrefixRegistrationRequestValidatorException("MISSING REQUIRED ID Regex Pattern");
        }
        try {
            Pattern.compile(request.getIdRegexPattern());
        } catch (PatternSyntaxException ex) {
            PrefixRegistrationRequestValidatorException prefixRegistrationRequestValidatorException =
                    new PrefixRegistrationRequestValidatorException(String.format("MISSING REQUIRED ID Regex Pattern: %s", ex.getMessage()));
            prefixRegistrationRequestValidatorException.initCause(ex);
            throw prefixRegistrationRequestValidatorException;
        }
        return true;
    }
}