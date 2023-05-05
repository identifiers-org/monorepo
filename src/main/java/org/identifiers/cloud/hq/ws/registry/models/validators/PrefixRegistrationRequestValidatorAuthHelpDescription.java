package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorAuthHelpDescription")
public class PrefixRegistrationRequestValidatorAuthHelpDescription implements PrefixRegistrationRequestValidator {
    public static final int DESCRIPTION_CONTENT_MIN_CHARS = 50;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.isProtectedUrls()) {
            if (request.getAuthHelpDescription() == null ||
                    request.getAuthHelpDescription().length() < DESCRIPTION_CONTENT_MIN_CHARS) {
                throw new PrefixRegistrationRequestValidatorException(
                        String.format("Authentication description must be longer than %d characters",
                                DESCRIPTION_CONTENT_MIN_CHARS));
            }
        }
        return true;
    }
}
