package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.apache.commons.validator.routines.EmailValidator;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:19
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class RequesterValidatorEmail implements RequesterValidator {
    @Override
    public boolean validate(Requester requester) throws RequesterValidatorException {
        if (requester.getEmail() == null) {
            throw new RequesterValidatorException("MISSING REQUIRED Requester e-mail address");
        }
        if (!EmailValidator.getInstance().isValid(requester.getEmail())) {
            throw new RequesterValidatorException(String.format("'%s' is not a valid email address", requester.getEmail()));
        }
        return true;
    }
}
