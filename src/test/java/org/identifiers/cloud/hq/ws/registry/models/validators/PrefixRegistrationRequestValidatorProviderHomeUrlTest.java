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
public class PrefixRegistrationRequestValidatorProviderHomeUrlTest {

    PrefixRegistrationRequestValidatorProviderHomeUrl validator
            = new PrefixRegistrationRequestValidatorProviderHomeUrl();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setProviderHomeUrl(p);

    @Test
    public void testNullProviderHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void testBlankProviderHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void testNotUrlProviderHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("Not a URL"));
    }

    @Test
    public void testNotHttpProviderHomeUrl() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("file://filename"));
    }

    @Test
    public void testValidProviderHomeUrl() {
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk")));
        assertTrue(validator.validate(fnc.apply("http://ebi.ac.uk")));
    }
}