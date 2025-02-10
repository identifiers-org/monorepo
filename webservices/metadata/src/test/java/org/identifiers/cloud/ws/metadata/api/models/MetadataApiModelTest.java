package org.identifiers.cloud.ws.metadata.api.models;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.identifiers.cloud.commons.messages.models.Recommendation;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.identifiers.cloud.commons.messages.requests.metadata.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = {TestRedisServer.class})
class MetadataApiModelTest {
    @Autowired
    MetadataApiModel model;

    @MockBean
    ResolverService resolverService;

    WireMockServer wireMockServer = new WireMockServer(9999);

    @BeforeEach
    void setUpWireMocks() throws FileNotFoundException {
        wireMockServer.start();

        var file = ResourceUtils.getFile("classpath:page-mocks/page_with_metadata.html");
        var fileInputStream = new FileInputStream(file);
        var inputStreamReader = new InputStreamReader(fileInputStream);
        String html = new BufferedReader(inputStreamReader).lines().collect(Collectors.joining());
        wireMockServer.stubFor(WireMock.get(urlMatching("/.*")).willReturn(ok()));
        wireMockServer.stubFor(WireMock.get("/").willReturn(ok(html)));
    }

    @AfterEach
    void tearDownWireMocks() {
        wireMockServer.stop();
    }

    @Test
    void testGetMetadataFor() {
        var response = getValidResolverResponse();
        Mockito.when(resolverService.requestCompactIdResolution(anyString())).thenReturn(response);

        var res = model.getMetadataFor("compact_id");
        assertTrue(res.getHttpStatus().is2xxSuccessful());
    }

    @Test
    void testGetMetadataForRawRequest() {
        var response = getValidResolverResponse();
        Mockito.when(resolverService.requestResolutionRawRequest(anyString())).thenReturn(response);

        var res = model.getMetadataForRawRequest("raw request");
        assertTrue(res.getHttpStatus().is2xxSuccessful());
    }

    @Test
    void testGetMetadataForUrl() {
        var serverUrl = wireMockServer.baseUrl();

        var payload = new RequestFetchMetadataForUrlPayload();
        payload.setUrl(serverUrl);
        var request = ServiceRequest.of(payload);

        var res = model.getMetadataForUrl(request);
        assertTrue(res.getHttpStatus().is2xxSuccessful() || res.getHttpStatus().equals(HttpStatus.NOT_FOUND));
    }


    ServiceResponse<ResponseResolvePayload> getValidResolverResponse() {
        var serverUrl = wireMockServer.baseUrl();

        ResolvedResource resolvedResource = new ResolvedResource()
                .setRecommendation(new Recommendation().setRecommendationIndex(100))
                .setCompactIdentifierResolvedUrl(serverUrl);
        ResponseResolvePayload payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.singletonList(resolvedResource));
        return ServiceResponse.of(payload);
    }
}