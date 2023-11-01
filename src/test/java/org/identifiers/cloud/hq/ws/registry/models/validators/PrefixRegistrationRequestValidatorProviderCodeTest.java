package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.Assert.assertTrue;


@SpringBootTest
@RunWith(JUnit4.class)
public class PrefixRegistrationRequestValidatorProviderCodeTest {
    PrefixRegistrationRequestValidatorProviderCode validator =
            new PrefixRegistrationRequestValidatorProviderCode();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setProviderCode(p);

    @Test
    public void validateNullProviderCode() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void validateBlankProviderCode() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void validateValidProviderCode() {
        assertTrue(validator.validate(fnc.apply("pcode")));
    }
}