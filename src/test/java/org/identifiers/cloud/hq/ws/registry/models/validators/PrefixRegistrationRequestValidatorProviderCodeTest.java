package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PrefixRegistrationRequestValidatorProviderCodeTest {
    PrefixRegistrationRequestValidatorProviderCode validator =
            new PrefixRegistrationRequestValidatorProviderCode();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setProviderCode(p);

    @Test
    void validateNullProviderCode() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(null))
        );
    }

    @Test
    void validateBlankProviderCode() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(""))
        );
    }

    @Test
    void validateValidProviderCode() {
        assertTrue(validator.validate(fnc.apply("pcode")));
    }
}