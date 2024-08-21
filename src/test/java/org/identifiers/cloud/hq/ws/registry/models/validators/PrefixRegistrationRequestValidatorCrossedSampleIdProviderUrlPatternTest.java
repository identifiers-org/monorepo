package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPatternTest {
    @SpyBean
    ResourceRepository repository;

    @Autowired
    PrefixRegistrationRequestValidatorCrossedSampleIdProviderUrlPattern validator;

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload()
                    .setSampleId("123").setProviderUrlPattern(p);

    @BeforeEach
    void interceptSimilarityFunction() {
        doReturn(null).when(repository).findSimilarByUrlPattern(anyString(), anyDouble());
    }

    @Test
    void validate() {
        //FIXME: HTTP request should be mocked, but current Mockito version doesn't allow mocking static functions.
        // Refactoring is not worth it for the moment
        assertTrue(validator.validate(fnc.apply("https://google.com?q={$id}")));
    }
}