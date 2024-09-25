package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;

import java.util.Optional;

@RequiredArgsConstructor
public class MinLengthValidator extends SingleValueValidator {
    final int minLength;

    @Override
    public Optional<String> validate(String value, String valueLabel) {
        if (StringUtils.isBlank(value) || lengthWithoutRedundantWhitespace(value) < minLength) {
            return Optional.of(valueLabel + " MUST be longer than " + minLength + " characters");
        }
        return Optional.empty();
    }

    private static int lengthWithoutRedundantWhitespace(String value) {
        return value.trim().replaceAll("\\s+", " ").length();
    }
}
