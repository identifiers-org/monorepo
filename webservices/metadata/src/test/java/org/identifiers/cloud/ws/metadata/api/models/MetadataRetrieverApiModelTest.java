package org.identifiers.cloud.ws.metadata.api.models;

import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//TODO mock retrievers
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestRedisServer.class)
class MetadataRetrieverApiModelTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ResolverService resolverService;

    final ParsedCompactIdentifier pci = new ParsedCompactIdentifier()
            .setNamespace("uniprot")
            .setLocalId("P12345")
            .setRawRequest("uniprot:P12345");

    @BeforeEach
    void setupMock(){
        var payload = new ResponseResolvePayload()
                .setResolvedResources(Collections.emptyList())
                .setParsedCompactIdentifier(pci);
        var response = ServiceResponse.of(payload);
        doReturn(response)
                .when(resolverService)
                .requestResolutionRawRequest(pci.getRawRequest());
    }

    @Test
    void testListOfRetrievers() throws Exception {
        mockMvc.perform(get("/retrievers/"+pci.getRawRequest()))
//                .andDo(print())
                .andExpect(handler().methodName("getRetrieversFor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.payload.parsedCompactIdentifier.namespace",
                        is(pci.getNamespace()))
                )
                .andExpect(jsonPath(
                        "$.payload.ableRetrievers",
                        not(empty()))
                );
    }

    @Test
    void testTogoIdParsedMetadata() throws Exception {
        mockMvc.perform(get("/retrievers/togoid/"+pci.getRawRequest()))
//                .andDo(print())
                .andExpect(handler().methodName("getRetrieverParsedMetadata"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void testTogoIdRawMetadata() throws Exception {
        mockMvc.perform(get("/retrievers/togoid/raw/"+pci.getRawRequest()))
//                .andDo(print())
                .andExpect(handler().methodName("getRetrieverRawMetadata"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

}