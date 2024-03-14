package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorAdditionalInformationTest {
    PrefixRegistrationRequestValidatorAdditionalInformation validator =
            new PrefixRegistrationRequestValidatorAdditionalInformation();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setAdditionalInformation(p);

    @Test
    void testBlankAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply("")));
    }

    @Test
    void testNullAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply(null)));
    }

    @Test
    void testValidAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply("Additional information")));
    }
}