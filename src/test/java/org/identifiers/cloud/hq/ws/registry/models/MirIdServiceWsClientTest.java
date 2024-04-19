package org.identifiers.cloud.hq.ws.registry.models;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.startsWithIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
abstract class MirIdServiceWsClientTest {

    @Autowired
    @Qualifier("miridRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    MirIdServiceWsClient client;

    @Autowired
    Environment env;

    MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void testMintRequestIsAuthenticated() {
        final String mockedMirId = "MIR:12345678";
        String endpoint = client.getWsMirIdMintingUrl();
        var assertion = server.expect(once(), requestTo(endpoint));
        if (env.matchesProfiles("authenabled")) {
            assertion.andExpect(header("authorization",  startsWithIgnoringCase("Bearer")));
        }
        assertion.andRespond(withStatus(HttpStatus.OK)
                .body(mockedMirId)
                .contentType(MediaType.TEXT_PLAIN));

        String mintedId = client.mintId();

        server.verify();

        assertEquals(mockedMirId, mintedId);
    }
}


class AuthDisabledMirIdServiceWsClientTest extends MirIdServiceWsClientTest {}

@Profile("authenabled")
@Disabled("Disabled because auth server must be mocked")
class AuthEnabledMirIdServiceWsClientTest extends MirIdServiceWsClientTest {}