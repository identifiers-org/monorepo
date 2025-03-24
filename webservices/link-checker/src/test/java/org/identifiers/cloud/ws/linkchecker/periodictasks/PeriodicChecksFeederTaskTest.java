package org.identifiers.cloud.ws.linkchecker.periodictasks;

import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class PeriodicChecksFeederTaskTest {
    PeriodicChecksFeederTask periodicChecksFeederTask;
    BlockingDeque<LinkCheckRequest> linkCheckRequestQueue = new LinkedBlockingDeque<>();

    @BeforeEach
    void setUp() {
        var mockedPayload = new ResponseResolvePayload();
        var resources = List.of(
                new ResolvedResource()
                        .setId(1)
                        .setCompactIdentifierResolvedUrl("http://valid.com")
                        .setResourceHomeUrl("http://valid.com")
                        .setDeprecatedResource(false)
                        .setDeprecatedNamespace(false)
                        .setProtectedUrls(false),
                new ResolvedResource()
                        .setId(2)
                        .setCompactIdentifierResolvedUrl("http://deprecatedResource.com")
                        .setResourceHomeUrl("http://deprecatedResource.com")
                        .setDeprecatedResource(true)
                        .setDeprecatedNamespace(false)
                        .setProtectedUrls(false),
                new ResolvedResource()
                        .setId(3)
                        .setCompactIdentifierResolvedUrl("http://deprecatedNamespace.com")
                        .setResourceHomeUrl("http://deprecatedNamespace.com")
                        .setDeprecatedResource(false)
                        .setDeprecatedNamespace(true)
                        .setProtectedUrls(false),
                new ResolvedResource()
                        .setId(4)
                        .setCompactIdentifierResolvedUrl("http://protected.com")
                        .setResourceHomeUrl("http://protected.com")
                        .setDeprecatedResource(false)
                        .setDeprecatedNamespace(false)
                        .setProtectedUrls(true)
        );
        mockedPayload.setResolvedResources(resources);


        var mockedResponse = ServiceResponse.of(mockedPayload);
        ResolverService resolverService = mock();
        doReturn(mockedResponse).when(resolverService).getAllSampleIdsResolved();

        periodicChecksFeederTask = new PeriodicChecksFeederTask(
                linkCheckRequestQueue,
                resolverService,
                Duration.ofMinutes(10),
                Duration.ofMinutes(10),
                Duration.ofMinutes(10)
        );
    }

    @Test
    void feedsTwoRequestsPerNonDeprecatedResource() {
        periodicChecksFeederTask.run();

        assertEquals(4, linkCheckRequestQueue.size());
        var request = linkCheckRequestQueue.poll();
        assertNotNull(request);
        assertEquals("1", request.getResourceId());
        request = linkCheckRequestQueue.poll();
        assertNotNull(request);
        assertEquals("1", request.getProviderId());

        request = linkCheckRequestQueue.poll();
        assertNotNull(request);
        assertEquals("4", request.getResourceId());
        assertTrue(request.shouldAccept401or403());
        request = linkCheckRequestQueue.poll();
        assertNotNull(request);
        assertEquals("4", request.getProviderId());
        assertFalse(request.shouldAccept401or403());
    }

}