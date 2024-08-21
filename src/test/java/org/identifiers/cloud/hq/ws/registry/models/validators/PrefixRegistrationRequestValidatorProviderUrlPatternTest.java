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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PrefixRegistrationRequestValidatorProviderUrlPatternTest {
    @SpyBean
    ResourceRepository repository;
    @Autowired
    PrefixRegistrationRequestValidatorProviderUrlPattern validator;

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            p -> new ServiceRequestRegisterPrefixPayload().setProviderUrlPattern(p);

    @BeforeEach
    void interceptSimilarityFunction() {
        doReturn(null).when(repository).findSimilarByUrlPattern(anyString(), anyDouble());
    }

    @Test
    void testNullProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(null));
        });
    }

    @Test
    void testBlankProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply(""));
        });
    }

    @Test
    void testNotUrlProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("Not a URL"));
        });
    }

    @Test
    void testNotHttpProviderUrlPattern() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("file://filename"));
        });
    }

    @Test
    void testProviderUrlPatternWithoutPlaceholder() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            validator.validate(fnc.apply("https://ebi.ac.uk"));
        });
    }

    @Test
    void testValidProviderUrlPattern() {
        assertTrue(validator.validate(fnc.apply("https://ebi.ac.uk/{$id}")));
    }
}