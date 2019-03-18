package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 12:13
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class PrefixRegistrationRequestValidatorInstitutionName implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getInstitutionName() == null) {
            throw new PrefixRegistrationRequestValidatorException("Institution Name is MISSING");
        }
        return false;
    }
}
