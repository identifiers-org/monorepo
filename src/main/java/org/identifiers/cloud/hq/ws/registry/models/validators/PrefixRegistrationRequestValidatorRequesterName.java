package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-04-03 13:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorRequesterName")
public class PrefixRegistrationRequestValidatorRequesterName implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        try {
            return RequesterValidatorFactory.getNameValidator().validate(request.getRequester());
        } catch (RequesterValidatorException e) {
            throw new PrefixRegistrationRequestValidatorException(e.getMessage());
        }
    }
}
