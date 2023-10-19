package org.identifiers.cloud.ws.metadata.models;

import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.identifiers.cloud.libapi.models.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Collections;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.models
 * Timestamp: 2018-02-09 13:42
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class IdResolverTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    IdResolver idResolver;

    @MockBean
    ResolverService resolverService;

    @Test
    public void testValidId() {
        ResponseResolvePayload payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.singletonList(new ResolvedResource()));
        ServiceResponseResolve response = (ServiceResponseResolve) new ServiceResponseResolve()
                .setApiVersion("1.0")
                .setPayload(payload);
        Mockito.when(resolverService.requestCompactIdResolution(Mockito.anyString()))
                                    .thenReturn(response);
        assertFalse("Valid Compact ID is resolved",
                idResolver.resolve("valid_id").isEmpty());
    }

    @Test
    public void testInvalidId() {
        ResponseResolvePayload payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.emptyList());
        ServiceResponseResolve response = (ServiceResponseResolve) new ServiceResponseResolve()
                .setApiVersion("1.0")
                .setPayload(payload);
        Mockito.when(resolverService.requestCompactIdResolution(Mockito.anyString())).thenReturn(response);

        assertTrue("Invalid Compact ID doesn't resolve",
                idResolver.resolve("invalid_id").isEmpty());
    }
}