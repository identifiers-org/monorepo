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
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorProviderName")
public class PrefixRegistrationRequestValidatorProviderName implements PrefixRegistrationRequestValidator {
    // Up to this point, a developer may be wondering why I return a value that it is not used, because in case a
    // validator "fails", I notify the "not valid" state and also the reason via an exception. It could have been done
    // by using a POJO that contains a boolean 'valid' to flag whether the attribute that was tested is valid or not, a
    // string to tell the client code why the validation failed, and then leave the exception mechanism for reporting
    // only actual exceptional situations that happened within the process. But, in the current iteration of this
    // component, I only need to know if it failed (in order to continue or not), and it case it did fail, I always
    // want to know why. So that's why I decided to do it via this coding style.
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
        if (request.getProviderName() == null) {
            throw new PrefixRegistrationRequestValidatorException("The name of the resource (provider name) is MISSING");
        } else if (request.getProviderName().length() == 0) {
            throw new PrefixRegistrationRequestValidatorException("Provider name cannot be empty");
        }
        return true;
    }
}
