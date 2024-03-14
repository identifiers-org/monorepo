package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorInstitutionHomeUrlTest {

    PrefixRegistrationRequestValidatorInstitutionHomeUrl validator =
            new PrefixRegistrationRequestValidatorInstitutionHomeUrl();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setInstitutionHomeUrl(p);

    @Test
    void testNullInstitutionHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(null))
        );
    }

    @Test
    void testBlankInstitutionHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply(""))
        );
    }

    @Test
    void testNotUrlInstitutionHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply("Not a URL"))
        );
    }

    @Test
    void testNotHttpInstitutionHomeUrl() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () ->
                validator.validate(fnc.apply("file://filename"))
        );
    }

    @Test
    void testValidInstitutionHomeUrl() {
        //FIXME: These Http calls should be mocked
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk")));
        assertTrue(validator.validate(fnc.apply("http://ebi.ac.uk")));
    }
}