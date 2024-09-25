package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import org.apache.commons.lang3.ArrayUtils;
import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class LocationCodeValidator extends SingleValueValidator {
    @Override
    public Optional<String> validate(String countryCode, String valueLabel) {
        boolean isValid = Arrays.stream(Locale.getISOCountries()).anyMatch(countryCode::equalsIgnoreCase);
        if (isValid) {
            return Optional.empty();
        }
        return Optional.of("Invalid code for " + valueLabel + ", please use one of the 2 digit ISO 3166 codes");
    }


}
