package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:25
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class RequesterValidatorFullValidator implements RequesterValidator {
    @Override
    public boolean validate(Requester requester) throws RequesterValidatorException {
        List<String> errors = new ArrayList<>();
        List<RequesterValidator> validators = Arrays.asList(new RequesterValidatorEmail(), new RequesterValidatorName());
        // Iterate over the validators
        for (RequesterValidator validator :
                validators) {
            try {
                validator.validate(requester);
            } catch (RequesterValidatorException e) {
                errors.add(e.getMessage());
            }
        }
        // Report errors if any
        if (!errors.isEmpty()) {
            throw new RequesterValidatorException(String.join("\n", errors));
        }
        return true;
    }
}
