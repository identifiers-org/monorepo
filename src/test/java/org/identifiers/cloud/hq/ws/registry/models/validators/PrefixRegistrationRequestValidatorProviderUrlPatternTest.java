package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorProviderUrlPatternTest {
    PrefixRegistrationRequestValidatorProviderUrlPattern validator =
            new PrefixRegistrationRequestValidatorProviderUrlPattern();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setProviderUrlPattern(p);

    @Test
    void testNullProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(null));
        });
    }

    @Test
    void testBlankProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(""));
        });
    }

    @Test
    void testNotUrlProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("Not a URL"));
        });
    }

    @Test
    void testNotHttpProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("file://filename"));
        });
    }

    @Test
    void testProviderUrlPatternWithoutPlaceholder() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("https://ebi.ac.uk"));
        });
    }

    @Test
    void testValidProviderUrlPattern() {
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk/{$id}")));
    }
}