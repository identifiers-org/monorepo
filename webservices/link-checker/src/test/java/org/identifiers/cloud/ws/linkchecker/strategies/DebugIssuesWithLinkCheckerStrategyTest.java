package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.identifiers.cloud.ws.linkchecker.TestRedisServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Disabled("This is meant to be used by devs to debug problematic URLs")
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
        properties = {
                "logging.level.org.identifiers.cloud.ws.linkchecker.strategies=DEBUG",
                "logging.level.org.identifiers.cloud.commons.urlchecking=DEBUG",
                "org.identifiers.cloud.ws.linkchecker.daemon.websiteswithtrustedcerts=",
                "org.identifiers.cloud.ws.linkchecker.daemon.periodiclinkcheckingtask.strategy=multi-user-agent",
                "org.identifiers.cloud.ws.linkchecker.daemon.websiteswithtrustedcerts=https://icd.who.int/browse10/2019/en#/C34",
        },
        classes = { TestRedisServer.class }
)
class DebugIssuesWithLinkCheckerStrategyTest {
    @Autowired
    LinkCheckerStrategy linkCheckerStrategy;

    static final List<String> UNPROTECTED_URLS = List.of();

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
        assertTrue(report.isUrlAssessmentOk(), "Assessment should be OK");
        assertTrue(HttpStatus.valueOf(report.getHttpStatus()).is2xxSuccessful(), "Status should be OK");
    }

    static final List<String> PREFIXES_TO_TEST = List.of();
    static Stream<Arguments> linksFromPrefixes() {
        var restTemplate = new RestTemplate();
        var typeRef = new ParameterizedTypeReference<ServiceResponse<ResolverDatasetPayload>>() {};
        var response = restTemplate.exchange(
                "https://registry.api.identifiers.org/resolutionApi/getResolverDataset",
                HttpMethod.GET, null, typeRef);

        assert response.getStatusCode().is2xxSuccessful() &&
                response.getBody() != null &&
                response.getBody().getPayload() != null: "Response is successful";

        return response.getBody().getPayload().getNamespaces().stream()
                .filter(n -> PREFIXES_TO_TEST.contains(n.getPrefix()))
                .flatMap(n -> n.getResources().stream())
                .flatMap(r -> {
                    var sampleUrl = r.getUrlPattern().replace("{$id}", r.getSampleId());
                    return Stream.of(
                            Arguments.of(sampleUrl, r.isProtectedUrls()),
                            Arguments.of(r.getResourceHomeUrl(), false)
                    );
                });
    }

    @MethodSource("linksFromPrefixes")
    @ParameterizedTest(name="{0} (Accept401or403: {1})")
    void checkNamespaceUrls(String urlString, boolean accept401or403) throws MalformedURLException {
        checkStrategyForUrl(urlString, accept401or403);
    }
}