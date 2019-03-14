package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the interface of any validation strategy for prefix registration validation requests (payload)
 */
public interface PrefixRegistrationRequestValidatorStrategy extends PrefixRegistrationRequestValidator {
    default List<PrefixRegistrationRequestValidator> getValidationChain() {
        return new ArrayList<>();
    }

    @Override
    default boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
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
                .collect(Collectors.toList())
                .stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        if (!errors.isEmpty()) {
            // Report the errors
            throw new PrefixRegistrationRequestValidatorException(String.join("\n", errors));
        }
        return true;
    }
}
