package org.identifiers.cloud.ws.metadata.periodictasks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.identifiers.cloud.ws.metadata.data.models.MetadataExtractionRequest;
import org.identifiers.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestRedisServer.class)
class MetadataCollectorTest {
    @Autowired
    MetadataCollector metadataCollector;

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

    @Test
    void testAttendMetadataExtractionRequestWithValidUrl() {
        MetadataExtractionRequest request = new MetadataExtractionRequest();
        request.setAccessUrl(wireMockServer.baseUrl() + "/page_with_metadata.html");
        request.setResolutionPath("resolution path");
        request.setTimestamp(Timestamp.from(Instant.now()));
        request.setResourceId("resource id");
        MetadataExtractionResult res = metadataCollector.attendMetadataExtractionRequest(request);
        assertTrue(HttpStatus.valueOf(res.getHttpStatus()).is2xxSuccessful());
    }
}