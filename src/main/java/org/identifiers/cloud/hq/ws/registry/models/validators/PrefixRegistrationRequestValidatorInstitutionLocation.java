package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 12:32
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorInstitutionLocation")
public class PrefixRegistrationRequestValidatorInstitutionLocation implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getInstitutionLocation() == null) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("Institution Location information is REQUIRED, but it's MISSING");
        }
        // TODO - Actually validate the provided location using ISO 3166/MA Alpha-2 Country Codes
        return true;
    }
}
