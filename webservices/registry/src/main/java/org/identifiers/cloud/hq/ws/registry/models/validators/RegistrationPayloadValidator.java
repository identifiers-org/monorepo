package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;

import java.util.Optional;
import java.util.function.Function;

public abstract class RegistrationPayloadValidator implements RegistrationValidationChainItem {
    /**
     * Perform validation on resource request payload
     * @param request to be validated
     * @param valueLabel name of attribute being validated
     * @return Optional with error message if invalid, empty otherwise
     */
    public abstract Optional<String> validate(ServiceRequestRegisterResourcePayload request, String valueLabel);


    /**
     * Perform validation on namespace request payload
     * @param request to be validated
     * @param valueLabel name of attribute being validated
     * @return Optional with error message if invalid, empty otherwise
     */
    public abstract Optional<String> validate(ServiceRequestRegisterPrefixPayload request, String valueLabel);





    @Override
    public final Optional<String> validate(ServiceRequestRegisterPrefixPayload request,
                                           Function<ServiceRequestRegisterPrefixPayload, String> prefixValueGetter,
                                           String valueLabel) {
        return validate(request, valueLabel);
    }

    @Override
    public final Optional<String> validate(ServiceRequestRegisterResourcePayload request,
                                           Function<ServiceRequestRegisterResourcePayload, String> prefixValueGetter,
                                           String valueLabel) {
        return validate(request, valueLabel);
    }
}
