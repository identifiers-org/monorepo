package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;

import java.util.Optional;
import java.util.function.Function;

public abstract class SingleValueValidator implements RegistrationValidationChainItem {
    /**
     * Validates a value of parameter type
     * @param value to be validated
     * @param valueLabel Label for value to be used in error message
     * @return Optional error string. Value is present if value is invalid.
     */
    public abstract Optional<String> validate(String value, String valueLabel);





    @Override
    public final Optional<String> validate(ServiceRequestRegisterPrefixPayload request,
                                           Function<ServiceRequestRegisterPrefixPayload, String> valueGetter,
                                           String valueLabel) {
        return validate(valueGetter.apply(request), valueLabel);
    }

    @Override
    public final Optional<String> validate(ServiceRequestRegisterResourcePayload request,
                                           Function<ServiceRequestRegisterResourcePayload, String> valueGetter,
                                           String valueLabel) {
        return validate(valueGetter.apply(request), valueLabel);
    }
}
