package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:59
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId")
public class PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId implements PrefixRegistrationRequestValidator {
    private static Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId.class);

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        List<String> errors = new ArrayList<>();
        // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
        // Check Example Identifier
        try {
            new PrefixRegistrationRequestValidatorSampleId().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // Check Regex Pattern
        try {
            new PrefixRegistrationRequestValidatorIdRegexPattern().validate(request);
        } catch (PrefixRegistrationRequestValidatorException e) {
            errors.add(e.getMessage());
        }
        // Report errors if any
        if (!errors.isEmpty()) {
            throw new PrefixRegistrationRequestValidatorException(String.join("\n", errors));
        }
        // Cross-validation
        logger.debug("Validating regex pattern '{}' against '{}'", request.getIdRegexPattern(), request.getSampleId());
        Pattern pattern;
        try {
            pattern = Pattern.compile(request.getIdRegexPattern());
        } catch (PatternSyntaxException ex) {
            throw new PrefixRegistrationRequestValidatorException(String.format("This regex is invalid: %s", ex.getMessage()));
        }
        Matcher matcher = pattern.matcher(request.getSampleId());
        if (!matcher.matches()) {
            throw new PrefixRegistrationRequestValidatorException(String.format("This regex does not match the Sample Id", request.getSampleId(), request.getIdRegexPattern()));
        }
        return true;
    }
}
