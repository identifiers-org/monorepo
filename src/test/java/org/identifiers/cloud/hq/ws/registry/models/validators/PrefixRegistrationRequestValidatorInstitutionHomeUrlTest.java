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
public class PrefixRegistrationRequestValidatorInstitutionHomeUrlTest {

    PrefixRegistrationRequestValidatorInstitutionHomeUrl validator =
            new PrefixRegistrationRequestValidatorInstitutionHomeUrl();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setInstitutionHomeUrl(p);

    @Test
    public void testNullInstitutionHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void testBlankInstitutionHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void testNotUrlInstitutionHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("Not a URL"));
    }

    @Test
    public void testNotHttpInstitutionHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("file://filename"));
    }

    @Test
    public void testValidInstitutionHomeUrl() {
        //FIXME: These Http calls should be mocked
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk")));
        assertTrue(validator.validate(fnc.apply("http://ebi.ac.uk")));
    }
}