package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.linkchecker.TestRedisServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Disabled("This is meant to be used by devs to debug why requests are failing for specific URLs")
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
        properties = {"logging.level.org.identifiers.cloud.ws.linkchecker.strategies=DEBUG"},
        classes = { TestRedisServer.class }
)
class DebugIssuesWithLinkCheckerStrategyTest {
    @Autowired
    LinkCheckerStrategy linkCheckerStrategy;

    static final List<String> UNPROTECTED_URLS = List.of(
            "https://www.uniprot.org/",
            "http://purl.uniprot.org/uniprot/P0DP23"
    );

    static final List<String> PROTECTED_URLS = List.of();

    static Stream<Arguments> getLinksToCheck() {
        return Stream.concat(
                UNPROTECTED_URLS.stream().map(s -> Arguments.of(s, false)),
                PROTECTED_URLS.stream().map(s -> Arguments.of(s, true))
        );
    }

    @ParameterizedTest(name="{0} (Accept401or403: {1})")
    @MethodSource("getLinksToCheck")
    void checkStrategyForUrl(String urlString, boolean accept401or403) throws MalformedURLException {
        var url = new URL(urlString);
        var report = linkCheckerStrategy.check(url, accept401or403);
        log.info("URL check report: {}", report);
        assertTrue(report.isUrlAssessmentOk());
        assertEquals(200, report.getHttpStatus());
    }
}