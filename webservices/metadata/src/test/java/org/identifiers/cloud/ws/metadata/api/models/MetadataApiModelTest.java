package org.identifiers.cloud.ws.metadata.api.models;

import org.identifiers.cloud.commons.messages.models.Recommendation;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.identifiers.cloud.commons.messages.requests.metadata.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = { TestRedisServer.class })
class MetadataApiModelTest {
    public static final String PAGE_WITH_METADATA_URL = "http://localhost:8082/page_with_metadata.html";
    @Autowired
    MetadataApiModel model;

    @MockBean
    ResolverService resolverService;

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
        var payload = new RequestFetchMetadataForUrlPayload();
        payload.setUrl(PAGE_WITH_METADATA_URL);
        var request = ServiceRequest.of(payload);

        var res = model.getMetadataForUrl(request);
        assertTrue(res.getHttpStatus().is2xxSuccessful() || res.getHttpStatus() == HttpStatus.NOT_FOUND);
    }


    ServiceResponse<ResponseResolvePayload> getValidResolverResponse() {
        ResolvedResource resolvedResource = new ResolvedResource()
                .setRecommendation(new Recommendation().setRecommendationIndex(100))
                .setCompactIdentifierResolvedUrl(PAGE_WITH_METADATA_URL);
        ResponseResolvePayload payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.singletonList(resolvedResource));
        return ServiceResponse.of(payload);
    }
}