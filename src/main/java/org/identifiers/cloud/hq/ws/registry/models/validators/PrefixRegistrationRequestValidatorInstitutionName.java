package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 12:13
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorInstitutionName")
public class PrefixRegistrationRequestValidatorInstitutionName implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getInstitutionName() == null) {
            throw new PrefixRegistrationRequestValidatorException("Institution name is MISSING");
        } else if (request.getInstitutionName().length() == 0) {
            throw new PrefixRegistrationRequestValidatorException("Institution name cannot be empty");
        }
        // Right now, there is no restriction on the length of the institution name
        // TODO Should we require a minimum length?
        return true;
    }
}
