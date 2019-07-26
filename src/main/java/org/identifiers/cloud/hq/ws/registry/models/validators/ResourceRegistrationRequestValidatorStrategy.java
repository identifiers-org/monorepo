package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 16:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ResourceRegistrationRequestValidatorStrategy extends ResourceRegistrationRequestValidator {
    default List<ResourceRegistrationRequestValidator> getValidationChain() {
        return new ArrayList<>();
    }

    @Override
    default boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        // List of errors for reporting to the client
        // Get all the validation errors
        List<String> errors = getValidationChain()
                .parallelStream()
                .map(validator -> {
                    try {
                        validator.validate(request);
                    } catch (PrefixRegistrationRequestValidatorException e) {
                        return e.getMessage();
                    }
                    return null;
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
                // NOTE - There is an optimization here, where I filter before collecting, to avoid collection of nulls, and a second round.
        if (!errors.isEmpty()) {
            // Report the errors
            throw new PrefixRegistrationRequestValidatorException(String.join("\n", errors));
        }
        return true;
    }
}
