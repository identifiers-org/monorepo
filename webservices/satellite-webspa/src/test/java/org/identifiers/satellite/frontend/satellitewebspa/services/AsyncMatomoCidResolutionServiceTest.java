package org.identifiers.satellite.frontend.satellitewebspa.services;

import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.MatomoException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.identifiers.satellite.frontend.satellitewebspa.services.AsyncMatomoCidResolutionService.MatomoTrackingInfo;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER;


@SpringBootTest(properties = {
    "org.identifiers.matomo.enabled=true",
    "org.identifiers.matomo.baseUrl=http://localhost:9999/matomo.php",
})
@WireMockTest(httpPort = 9999)
class AsyncMatomoCidResolutionServiceTest {
    @Autowired
    AsyncMatomoCidResolutionService service;

    @Test
    void doHandleCidResolution() {
        stubFor(post("/matomo.php").willReturn(ok()));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(25),
                () -> service.doHandleCidResolution(info())
        );
        assertFalse(future.isDone());

        future.join();
        assertTrue(future.isDone());
    }

    @Test
    void doHandleCidResolutionDelayed() {
        stubFor(post("/matomo.php").willReturn(ok().withFixedDelay(3000)));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(25),
                () -> service.doHandleCidResolution(info())
        );
        assertFalse(future.isDone());

        future.join();
        assertTrue(future.isDone());
    }

    @Test
    void doHandleCidResolutionFailedConnection() {
        stubFor(post("/matomo.php").willReturn(ok().withFault(CONNECTION_RESET_BY_PEER)));

        var future = assertTimeoutPreemptively(
                Duration.ofMillis(25),
                () -> service.doHandleCidResolution(info())
        );
        assertFalse(future.isDone());

        var exception = assertThrows(CompletionException.class, future::join);
        assertEquals(MatomoException.class, exception.getCause().getClass());
    }

    @Test
    void doHandleCidResolutionMultipleCallsAreDoneAsynchronously() {
        List<CompletableFuture<Void>> futures = new LinkedList<>();
        int nRequests = new Random().nextInt(750, 1250);

        stubFor(post("/matomo.php").willReturn(ok().withFixedDelay(200)));

        // Ensure submitting requests is done quickly
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
            IntStream.range(0, nRequests)
                .mapToObj(this::info)
                .map(service::doHandleCidResolution)
                .forEach(futures::add)
        );

        // Ensure all requests are sent to matomo backend
        futures.forEach(CompletableFuture::join);
        var serveEvents = getAllServeEvents();
        assertEquals(nRequests, serveEvents.size());

        // Ensure all IPs are not repeated for any reason
        var matcher = Pattern.compile("cip=(\\d+\\.\\d+\\.\\d+\\.\\d+)").matcher("");
        var ips = serveEvents.stream()
                .map(e -> e.getRequest().getBodyAsString())
                .map(s -> {
                    matcher.reset(s);
                    return matcher.find() ? matcher.group(1) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        assertEquals(nRequests, ips.size());
    }



    private MatomoTrackingInfo info(int i) {
        return new MatomoTrackingInfo(
                "test.com",
                "10.0.0."+i,
                "11.0.0.0",
                "android",
                "en",
                "no-referer",
                Collections.emptyList(),
                null,
                false
        );
    }

    private MatomoTrackingInfo info() {
        return info(0);
    }
}
