package org.identifiers.cloud.commons.urlchecking;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UrlCheckerTest {
    static UrlChecker urlChecker;

    @BeforeAll
    static void setUp() throws IOException {
//        System.setProperty("log4j.rootLogger", "debug");

        var sslFactory = HttpClientHelper.getBaseSSLFactoryBuilder(true).build();
        var httpClient = HttpClientHelper.getBaseHttpClientBuilder(sslFactory).build();
        urlChecker = new UrlChecker(httpClient);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://aopwiki.org/relationships/5"
    })
    void checkUrl(String url) {
        assertDoesNotThrow(() -> {
            var urlAssessment = urlChecker.check(url, true);
            assertNotNull(urlAssessment);
            if (!urlAssessment.isOk()) {
                System.out.println(urlAssessment.getError());
            } else {
                System.out.println("Response is valid");
            }
            assertTrue(urlAssessment.isOk());
        });
    }
}