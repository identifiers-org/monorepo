package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleIdTest {

    PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId validator =
            new PrefixRegistrationRequestValidatorCrossedIdRegexPatternAndSampleId();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setSampleId(p).setIdRegexPattern("\\d+");

    @Test
    void validateMatchingIdAndPatternPair() {
        assertTrue(validator.validate(fnc.apply("123")));
    }

    @Test
    void validateNonMatchingIdAndPatternPair() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                assertTrue(validator.validate(fnc.apply("abc")))
        );
    }
}