package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class RequesterValidatorName implements RequesterValidator {
    private static Logger logger = LoggerFactory.getLogger(RequesterValidatorName.class);

    @Override
    public boolean validate(Requester requester) throws RequesterValidatorException {
        if (requester.getName() == null || requester.getName().length() == 0) {
            logger.info("Requester name is required");
            throw new PrefixRegistrationRequestValidatorException("Requester name cannot be empty");
        }
        return true;
    }
}
