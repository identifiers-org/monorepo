package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;

import java.util.Optional;

public class EmailValidator extends SingleValueValidator {
    final org.apache.commons.validator.routines.EmailValidator validator =
            org.apache.commons.validator.routines.EmailValidator.getInstance();

    @Override
    public Optional<String> validate(String email, String valueLabel) {
        if (!validator.isValid(email)) {
            return Optional.of(email + " is not a valid email address");
        }
        return Optional.empty();
    }
}
