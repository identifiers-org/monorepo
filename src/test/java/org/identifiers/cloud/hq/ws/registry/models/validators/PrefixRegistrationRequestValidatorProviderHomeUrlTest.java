package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorProviderHomeUrlTest {

    PrefixRegistrationRequestValidatorProviderHomeUrl validator
            = new PrefixRegistrationRequestValidatorProviderHomeUrl();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setProviderHomeUrl(p);

    @Test
    void testNullProviderHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(null))
        );
    }

    @Test
    void testBlankProviderHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(""))
        );
    }

    @Test
    void testNotUrlProviderHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply("Not a URL"))
        );
    }

    @Test
    void testNotHttpProviderHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply("file://filename"))
        );
    }

    @Test
    void testValidProviderHomeUrl() {
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk")));
        assertTrue(validator.validate(fnc.apply("http://ebi.ac.uk")));
    }
}