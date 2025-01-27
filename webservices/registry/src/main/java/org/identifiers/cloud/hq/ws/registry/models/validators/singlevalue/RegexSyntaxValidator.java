package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;


import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexSyntaxValidator extends SingleValueValidator {
    @Override
    public Optional<String> validate(String regex, String valueLabel) {
        try {
            Pattern.compile(regex);
            return Optional.empty();
        } catch (PatternSyntaxException ex) {
            return Optional.of("Invalid regex: " + ex.getMessage());
        }
    }
}
