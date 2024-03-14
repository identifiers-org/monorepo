package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorProviderDescriptionTest {

    PrefixRegistrationRequestValidatorProviderDescription validator =
            new PrefixRegistrationRequestValidatorProviderDescription();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setProviderDescription(p);

    @Test()
    void testNullProviderDescription() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(null));
        });
    }

    @Test
    void testBlankProviderDescription() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(""));
        });
    }

    @Test
    void testShortProviderDescription() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("This is too short"));
        });
    }

    @Test
    void testValidProviderDescription() {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Integer mi dui, pulvinar eget nunc at, faucibus convallis orci. " +
                "Nulla facilisi. Mauris id risus tempus, vulputate velit non, viverra magna. " +
                "Donec accumsan iaculis elit ut efficitur. Nam ac tellus dignissim, sodales massa a, pharetra dolor. " +
                "Mauris gravida hendrerit nunc, eu vulputate est.";

        assertTrue(validator.validate(fnc.apply(desc)));
    }
}