package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 13:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Qualifier("ResourceRegistrationRequestValidatorAdditionalInformation")
public class ResourceRegistrationRequestValidatorAdditionalInformation implements ResourceRegistrationRequestValidator {
    @Autowired
    @Qualifier("prefixRegistrationRequestValidatorAdditionalInformation")
    private PrefixRegistrationRequestValidator delegateValidator;

    @Override
    public boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        return delegateValidator.validate(ApiAndDataModelsHelper.getFrom(request));
    }
}
