package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPatternTest {

    PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern validator
            = new PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setSampleId("123").setProviderUrlPattern(p);

    @Test
    void validate() {
        //FIXME: HTTP request should be mocked, but current Mockito version doesn't allow mocking static functions.
        // Refactoring is not worth it for the moment
        assertTrue(validator.validate(fnc.apply("https://google.com?q={$id}")));
    }
}