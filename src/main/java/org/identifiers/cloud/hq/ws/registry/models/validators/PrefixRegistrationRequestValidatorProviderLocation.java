package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 11:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorProviderLocation")
public class PrefixRegistrationRequestValidatorProviderLocation implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getProviderLocation() == null) {
            throw new PrefixRegistrationRequestValidatorException("Provider Location MUST BE PRESENT, and follow ISO 3166/MA Alpha-2 Country Codes format");
        }
        // TODO - Actually validate the provided location using ISO 3166/MA Alpha-2 Country Codes
        return true;
    }
}
