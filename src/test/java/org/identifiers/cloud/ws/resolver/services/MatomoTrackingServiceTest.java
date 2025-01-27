package org.identifiers.cloud.ws.resolver.services;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.identifiers.cloud.ws.resolver.services.MatomoTrackingService.MatomoTrackingInfo;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.MatomoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "org.identifiers.matomo.enabled=true",
    "matomo.tracker.enabled=true",
    "org.identifiers.matomo.baseUrl=http://localhost:9999/matomo.php",
    "matomo.tracker.api-endpoint=http://localhost:9999/matomo.php"
})
@WireMockTest(httpPort = 9999)
class MatomoTrackingServiceTest {
    @Autowired
    MatomoTrackingService service;
    static MatomoTrackingInfo info = new MatomoTrackingInfo(
            "test.com",
            "10.0.0.0",
            "11.0.0.0",
            "android",
            "en",
            "no-referer",
            Collections.emptyList(),
            null,
            false
    );

    @Test
    void doHandleCidResolution() {
        stubFor(post("/matomo.php").willReturn(ok()));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(75),
                () -> service.doHandleCidResolution(info)
        );
        assertFalse(future.isDone());

        future.join();
        assertTrue(future.isDone());
    }

    @Test
    void doHandleCidResolutionDelayed() {
        stubFor(post("/matomo.php").willReturn(ok().withFixedDelay(3000)));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(75),
                () -> service.doHandleCidResolution(info)
        );
        assertFalse(future.isDone());

        future.join();
        assertTrue(future.isDone());
    }

    @Test
    void doHandleCidResolutionFailedConnection() {
        stubFor(post("/matomo.php").willReturn(ok().withFault(CONNECTION_RESET_BY_PEER)));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(75),
                () -> service.doHandleCidResolution(info)
        );
        assertFalse(future.isDone());

        var exception = assertThrows(CompletionException.class, future::join);
        assertEquals(MatomoException.class, exception.getCause().getClass());
    }
}