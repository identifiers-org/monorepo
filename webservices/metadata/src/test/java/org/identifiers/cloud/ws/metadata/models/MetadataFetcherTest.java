package org.identifiers.cloud.ws.metadata.models;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.models
 * Timestamp: 2018-02-12 13:28
 * ---
 */
@SpringBootTest(classes = {TestRedisServer.class})
class MetadataFetcherTest {
    WireMockServer wireMockServer = new WireMockServer(9999);

    @BeforeEach
    void setUpWireMocks() throws FileNotFoundException {
        wireMockServer.start();

        wireMockServer.stubFor(WireMock.get(urlMatching("/.*")).willReturn(ok()));
        for (var fname : Set.of("page_with_metadata.html", "page_with_metadata2.html")) {
            var file = ResourceUtils.getFile("classpath:page-mocks/"+fname);
            var fileInputStream = new FileInputStream(file);
            var inputStreamReader = new InputStreamReader(fileInputStream);
            String html = new BufferedReader(inputStreamReader).lines().collect(Collectors.joining());
            wireMockServer.stubFor(WireMock.get("/"+fname).willReturn(ok(html)));
        }
    }

    @AfterEach
    void tearDownWireMocks() {
        wireMockServer.stop();
    }

    // TODO - Fix this unit test or kill it
    @Autowired
    MetadataFetcher metadataFetcher;

    @Test
    void testValidMetadata() {
        // NOTE - Possible future extension here to make it more exhaustive
        // This unit test is too dependant on that particular URL to be up, as soon as this is working, I may
        // choose to deactivate this test
        getUrlsWithValidMetadata().parallelStream().forEach(validUrl -> {
            Object metadata = metadataFetcher.fetchMetadataFor(validUrl);
            assertThat(String.format("URL '%s' contains VALID metadata", validUrl), metadata, notNullValue());
        });
    }

    private List<String> getUrlsWithValidMetadata() {
        // TODO: Make port number dynamic after updating java to 17 and dependencies
        return Arrays.asList(
                wireMockServer.baseUrl()+"/page_with_metadata.html",
                wireMockServer.baseUrl()+"/page_with_metadata2.html");
    }
}