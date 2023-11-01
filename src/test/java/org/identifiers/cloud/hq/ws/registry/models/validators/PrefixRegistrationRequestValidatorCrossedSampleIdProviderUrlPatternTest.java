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
public class PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPatternTest {

    PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern validator
            = new PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setSampleId("123").setProviderUrlPattern(p);

    @Test
    public void validate() {
        //FIXME: HTTP request should be mocked, but current Mockito version doesn't allow mocking static functions.
        // Refactoring is not worth it for the moment
        validator.validate(fnc.apply("https://google.com?q={$id}"));
    }
}