package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class IsNotBlankValidator extends SingleValueValidator {

    @Override
    public Optional<String> validate(String string, String valueLabel) {
        if (!StringUtils.hasText(string)) {
            return  Optional.of(valueLabel + " is required");
        } else {
            return Optional.empty();
        }
    }
}
