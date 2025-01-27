package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;

import java.util.Optional;
import java.util.function.Function;

public interface RegistrationValidationChainItem {
    /**
     * Validate request
     * @param request to be validated
     * @param prefixValueGetter getter of value from request
     * @param valueLabel label of value being validated
     * @return error string if request invalid
     */
    Optional<String> validate(
            ServiceRequestRegisterPrefixPayload request,
            Function<ServiceRequestRegisterPrefixPayload, String> prefixValueGetter,
            String valueLabel
    );

    /**
     * Validate request
     * @param request to be validated
     * @param prefixValueGetter getter of value from request
     * @param valueLabel label of value being validated
     * @return error string if request invalid
     */
    Optional<String> validate(
            ServiceRequestRegisterResourcePayload request,
            Function<ServiceRequestRegisterResourcePayload, String> prefixValueGetter,
            String valueLabel
    );
}
