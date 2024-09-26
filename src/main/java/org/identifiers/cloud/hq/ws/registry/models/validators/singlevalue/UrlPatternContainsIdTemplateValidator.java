package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;

import java.util.Optional;

import static org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper.PROVIDER_URL_PATTERN_PLACEHOLDER_ID;

public class UrlPatternContainsIdTemplateValidator extends SingleValueValidator {
    @Override
    public Optional<String> validate(String value, String valueLabel) {
        if(value.contains(PROVIDER_URL_PATTERN_PLACEHOLDER_ID)) {
            return Optional.empty();
        } else {
            return Optional.of(valueLabel + " must include the placeholder " + PROVIDER_URL_PATTERN_PLACEHOLDER_ID);
        }
    }
}
