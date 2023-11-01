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
public class PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleIdTest {

    PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId validator =
            new PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setSampleId(p).setIdRegexPattern("\\d+");

    @Test
    public void validateMatchingIdAndPatternPair() {
        assertTrue(validator.validate(fnc.apply("123")));
    }

    @Test
    public void validateNonMatchingIdAndPatternPair() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        assertTrue(validator.validate(fnc.apply("abc")));
    }
}