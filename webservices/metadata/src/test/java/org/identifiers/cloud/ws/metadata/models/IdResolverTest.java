package org.identifiers.cloud.ws.metadata.models;

import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.models
 * Timestamp: 2018-02-09 13:42
 * ---
 */
@SpringBootTest()
class IdResolverTest {
    @Autowired
    IdResolver idResolver;

    @MockBean
    ResolverService resolverService;

    @Test
    void testValidId() {
        var payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.singletonList(new ResolvedResource()));
        var response = ServiceResponse.of(payload);

        Mockito.when(resolverService.requestCompactIdResolution(Mockito.anyString()))
                                    .thenReturn(response);
        assertFalse(idResolver.resolve("valid_id").isEmpty());
    }

    @Test
    void testInvalidId() {
        var payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.emptyList());
        var response = ServiceResponse.of(payload);

        Mockito.when(resolverService.requestCompactIdResolution(Mockito.anyString())).thenReturn(response);

        assertTrue(idResolver.resolve("invalid_id").isEmpty());
    }
}