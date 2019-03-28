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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorRequestedPrefix.class);

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getRequestedPrefix() == null) {
            logger.error("Invalid request for validating Requested Prefix, WITHOUT specifying a prefix");
            throw new PrefixRegistrationRequestValidatorException("MISSING Preferred Prefix");
        } else if (request.getRequestedPrefix().length() == 0) {
            logger.error("Invalid request for validating Requested Prefix, empty prefix");
            throw new PrefixRegistrationRequestValidatorException("Requested prefix cannot be empty");
        } else {
            // TODO: Must confirm this is the right pattern for prefixes.
            Pattern pattern = Pattern.compile("[a-z0-9_.]*");
            Matcher matcher = pattern.matcher(request.getRequestedPrefix());

            if (!matcher.matches()) {
                logger.error("Invalid request for validating Requested Prefix, wrong prefix");
                throw new PrefixRegistrationRequestValidatorException("Requested prefix can only contain lowercase " +
                        "characters, numbers, underscores and dots");
            }
        }

        String errorMessage = "--- no error message has been set ---";
        String shortErrorMessage = "--- no short error message has been set ---";
        try {
            Namespace foundNamespace = namespaceRepository.findByPrefix(request.getRequestedPrefix());
            if (foundNamespace != null) {
                if (foundNamespace.isDeprecated()) {
                    errorMessage = String.format("Prefix '%s' is DEPRECATED, for REACTIVATION, please, use a " +
                            "different " +
                            "API", request.getRequestedPrefix());
                    shortErrorMessage = String.format("Prefix '%s' is deprecated", request.getRequestedPrefix());
                    logger.error(errorMessage);
                    throw new PrefixRegistrationRequestValidatorException(shortErrorMessage);
                }
                errorMessage = String.format("Prefix Validation FAILED on prefix '%s', because it IS ALREADY " +
                        "REGISTERED", request.getRequestedPrefix());
                shortErrorMessage = String.format("Prefix '%s' already exists", request.getRequestedPrefix());
                logger.error(errorMessage);
                throw new PrefixRegistrationRequestValidatorException(shortErrorMessage);
            }
        } catch (RuntimeException e) {
            // TODO
            errorMessage = String.format("While validating prefix '%s', the following error occurred: '%s'",
                    request.getRequestedPrefix(), e.getMessage());
            shortErrorMessage = e.getMessage();
            logger.error(errorMessage);
            throw new PrefixRegistrationRequestValidatorException(shortErrorMessage);
        }
        return true;
    }
}
