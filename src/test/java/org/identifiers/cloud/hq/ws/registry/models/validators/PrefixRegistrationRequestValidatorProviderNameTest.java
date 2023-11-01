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
public class PrefixRegistrationRequestValidatorProviderNameTest {
    PrefixRegistrationRequestValidatorProviderName validator =
            new PrefixRegistrationRequestValidatorProviderName();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setProviderName(p);

    @Test
    public void validateNullProviderName() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void validateBlankProviderName() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void validateValidProviderName() {
        assertTrue(validator.validate(fnc.apply("EBI")));
    }
}