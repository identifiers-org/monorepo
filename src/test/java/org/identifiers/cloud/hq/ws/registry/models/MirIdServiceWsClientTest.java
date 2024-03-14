package org.identifiers.cloud.hq.ws.registry.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class MirIdServiceWsClientTest {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MirIdServiceWsClient client;

    MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testMintId() {
        final String mockedMirId = "MIR:12345678";
        String endpoint = client.getWsMirIdMintingUrl();

        server.expect(once(), requestTo(endpoint))
                .andRespond(withStatus(HttpStatus.OK)
                    .body(mockedMirId)
                    .contentType(MediaType.TEXT_PLAIN));

        String newMirId = client.mintId();

        server.verify();

        assertEquals(mockedMirId, newMirId);
    }
}