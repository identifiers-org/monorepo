package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
// We don't need qualifier here?
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorRequestedPrefix")
public class PrefixRegistrationRequestValidatorRequestedPrefix implements PrefixRegistrationRequestValidator {
    // TODO - Refactor this according to issue #16, at https://github.com/identifiers-org/cloud-hq-ws-registry/issues/16
    private static Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorRequestedPrefix.class);

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getRequestedPrefix() == null) {
            logger.error("Invalid request for validating Requested Prefix, WITHOUT specifying a prefix");
            throw new PrefixRegistrationRequestValidatorException("MISSING Preferred Prefix");
        }
        // I planned on reusing the error message, but I may use different messages for logging and the client
        String errorMessage = "--- no error message has been set ---";
        try {
            Namespace foundNamespace = namespaceRepository.findByPrefix(request.getRequestedPrefix());
            if (foundNamespace != null) {
                if (foundNamespace.isDeprecated()) {
                    errorMessage = String.format("Prefix '%s' is DEPRECATED, for REACTIVATION, please, use a " +
                            "different " +
                            "API", request.getRequestedPrefix());
                    logger.error(String.format("Prefix Validation FAILED on prefix '%s', because it ALREADY EXISTS " +
                            "and it's DEPRECATED", request.getRequestedPrefix()));
                    throw new PrefixRegistrationRequestValidatorException(errorMessage);
                }
                errorMessage = String.format("Prefix Validation FAILED on prefix '%s', because it IS ALREADY " +
                        "REGISTERED", request.getRequestedPrefix());
                logger.error(errorMessage);
                throw new PrefixRegistrationRequestValidatorException(errorMessage);
            }
        } catch (RuntimeException e) {
            // TODO
            errorMessage = String.format("While validating prefix '%s', the following error occurred: '%s'",
                    request.getRequestedPrefix(), e.getMessage());
            logger.error(errorMessage);
            throw new PrefixRegistrationRequestValidatorException(errorMessage);
        }
        return true;
    }
}
