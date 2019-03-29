package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("prefixRegistrationRequestValidatorRequester")
public class PrefixRegistrationRequestValidatorRequester implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
        if (request.getRequester() == null) {
            throw new PrefixRegistrationRequestValidatorException("MISSING REQUIRED Requester information");
        }
        // Delegate validation to specialized validator
        try {
            RequesterValidatorFactory.getDefaultValidator().validate(request.getRequester());
        } catch (RequesterValidatorException e) {
            throw new PrefixRegistrationRequestValidatorException(e.getMessage());
        }
        return true;
    }
}
