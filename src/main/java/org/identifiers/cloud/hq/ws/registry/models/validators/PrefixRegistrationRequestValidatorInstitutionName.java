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
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorInstitutionName")
public class PrefixRegistrationRequestValidatorInstitutionName implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getInstitutionName() == null) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("Institution name is MISSING");
        } else if (request.getInstitutionName().trim().isEmpty()) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("Institution name cannot be empty");
        }
        // Right now, there is no restriction on the length of the institution name
        // TODO Should we require a minimum length?
        return true;
    }
}
