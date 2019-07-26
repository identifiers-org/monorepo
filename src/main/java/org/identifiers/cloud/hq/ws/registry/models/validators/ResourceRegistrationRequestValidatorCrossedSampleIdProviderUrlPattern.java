package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 16:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Qualifier("ResourceRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
public class ResourceRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern implements ResourceRegistrationRequestValidator {
    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern")
    private PrefixRegistrationRequestValidator delegateValidator;

    @Override
    public boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        return delegateValidator.validate(ApiAndDataModelsHelper.getFrom(request));
    }
}
