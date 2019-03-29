package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO - It is ok right now to leave this annotations here, as the validator doesn't use any properties and such, but
// TODO - if these annotations are here to stay, then I should modify the validation strategies later on to not instantiate these components themselves but autowire them or get them injected via constructor
@Component
@Scope("prototype")
@Qualifier("prefixRegistrationRequestValidatorName")
public class PrefixRegistrationRequestValidatorName implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getName() == null) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("'Name' attribute must be provided");
        } else if (request.getName().length() == 0) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("Name cannot be empty");
        }
        return true;
    }
}
