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
public class PrefixRegistrationRequestValidatorInstitutionDescriptionTest {

    PrefixRegistrationRequestValidatorInstitutionDescription validator =
            new PrefixRegistrationRequestValidatorInstitutionDescription();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setInstitutionDescription(p);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testNullInstitutionDescription() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(null));
    }

    @Test
    public void testBlankInstitutionDescription() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply(""));
    }

    @Test
    public void testShortInstitutionDescription() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        validator.validate(fnc.apply("This is too short"));
    }

    @Test
    public void testValidInstitutionDescription() {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Integer mi dui, pulvinar eget nunc at, faucibus convallis orci. " +
                "Nulla facilisi. Mauris id risus tempus, vulputate velit non, viverra magna. " +
                "Donec accumsan iaculis elit ut efficitur. Nam ac tellus dignissim, sodales massa a, pharetra dolor. " +
                "Mauris gravida hendrerit nunc, eu vulputate est.";

        assertTrue(validator.validate(fnc.apply(desc)));
    }
}