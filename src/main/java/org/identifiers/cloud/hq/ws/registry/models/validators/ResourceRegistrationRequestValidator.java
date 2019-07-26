package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 11:01
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface models the contract that any validator in a resource registration request should meet.
 */
public interface ResourceRegistrationRequestValidator {
    // Default implementation, just as syntactic sugar
    default boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        return false;
    }
}
