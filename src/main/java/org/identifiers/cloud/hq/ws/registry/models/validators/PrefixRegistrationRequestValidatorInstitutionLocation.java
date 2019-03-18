package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 12:32
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class PrefixRegistrationRequestValidatorInstitutionLocation implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getInstitutionLocation() == null) {
            throw new PrefixRegistrationRequestValidatorException("Institution Location information is REQUIRED, but it's MISSING");
        }
        return false;
    }
}
