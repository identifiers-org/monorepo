package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class PrefixRegistrationRequestValidatorProviderNameTest {
    PrefixRegistrationRequestValidatorProviderName validator =
            new PrefixRegistrationRequestValidatorProviderName();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setProviderName(p);

    @Test
    void validateNullProviderName() {
        assertThrows(PrefixRegistrationRequestValidatorException.class,
                () -> validator.validate(fnc.apply(null))
        );
    }

    @Test
    void validateBlankProviderName() {
        assertThrows(PrefixRegistrationRequestValidatorException.class,
                () -> validator.validate(fnc.apply(""))
        );
    }

    @Test
    void validateValidProviderName() {
        assertTrue(validator.validate(fnc.apply("EBI")));
    }
}