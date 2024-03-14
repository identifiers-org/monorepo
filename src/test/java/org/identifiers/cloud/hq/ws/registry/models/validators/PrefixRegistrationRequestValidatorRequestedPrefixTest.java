package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PrefixRegistrationRequestValidatorRequestedPrefixTest {
    @MockBean
    NamespaceRepository namespaceRepository;

    @BeforeEach
    void setupMockedNamespaceRespository() {
        Namespace ns = new Namespace().setPrefix("namespace1");
        doReturn(null).when(namespaceRepository).findByPrefix(anyString());
        doReturn(ns).when(namespaceRepository).findByPrefix("namespace1");
    }

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorRequestedPrefix")
    PrefixRegistrationRequestValidator prefixValidator;

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setRequestedPrefix(p);

    @Test
    void testPrefixValidatorBlankString() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            prefixValidator.validate(fnc.apply(""));
        });
    }

    @Test
    void testPrefixValidatorUppercaseString() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            prefixValidator.validate(fnc.apply("UPPERCASE_PREFIX"));
        });
    }


    @Test
    void testPrefixValidatorNullString() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            prefixValidator.validate(fnc.apply(null));
        });
    }


    @Test
    void testPrefixValidatorNewValidPrefix() {
        assertTrue(prefixValidator.validate(fnc.apply("new_prefix")));
    }


    @Test
    void testPrefixValidatorExistingNamespace() {
        assertThrows(PrefixRegistrationRequestValidatorException.class, () -> {
            prefixValidator.validate(fnc.apply("namespace1"));
        });
    }
}