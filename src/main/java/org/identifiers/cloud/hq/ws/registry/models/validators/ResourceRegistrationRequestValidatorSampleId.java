package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-26 13:25
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// NOTE: It doesn't look like validators need to be of scope 'prototype', like in the case of prefix registration request
@Component
@Qualifier("ResourceRegistrationRequestValidatorSampleId")
public class ResourceRegistrationRequestValidatorSampleId implements ResourceRegistrationRequestValidator {
    // We borrow from prefix registration request API
    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorSampleId")
    private PrefixRegistrationRequestValidator delegateValidator;

    @Override
    public boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        // TODO
        return delegateValidator.validate(ApiAndDataModelsHelper.getFrom(request));
    }
}
