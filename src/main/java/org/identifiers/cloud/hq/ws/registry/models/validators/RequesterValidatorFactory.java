package org.identifiers.cloud.hq.ws.registry.models.validators;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class RequesterValidatorFactory {
    // TODO - This is a very stupid approach in the current situation, I should refactor this in the future
    public static RequesterValidator getDefaultValidator() {
        return new RequesterValidatorFullValidator();
    }

    public static RequesterValidator getNameValidator() {
        return new RequesterValidatorName();
    }

    public static RequesterValidator getEmailValidator() {
        return new RequesterValidatorEmail();
    }
}
