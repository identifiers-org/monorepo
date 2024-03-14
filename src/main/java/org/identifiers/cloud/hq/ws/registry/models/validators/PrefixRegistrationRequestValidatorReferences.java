package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("prefixRegistrationRequestValidatorReferences")
public class PrefixRegistrationRequestValidatorReferences implements PrefixRegistrationRequestValidator {
    private static final Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorReferences.class);

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
        // References are not enforced, so they just validate
        logger.info("References validation policy is VALID by default");
        if (request.getSupportingReferences() == null) {
            logger.info("No supportingReferences have been provided");
        } else {
            logger.info(String.format("#%d Reference entries have been provided", request.getSupportingReferences().length));
        }
        return true;
    }
}
