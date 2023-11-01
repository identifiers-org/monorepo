package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(JUnit4.class)
public class PrefixRegistrationRequestValidatorIdRegexPatternTest {

    PrefixRegistrationRequestValidatorIdRegexPattern validator
            = new PrefixRegistrationRequestValidatorIdRegexPattern();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setIdRegexPattern(p);

    @Test
    public void validateNullPattern() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void validateBlankPattern() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void validateNotAPattern() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("[]"));
    }

    @Test
    public void validateValidPattern() {
        validator.validate(fnc.apply("\\d+"));
    }
}