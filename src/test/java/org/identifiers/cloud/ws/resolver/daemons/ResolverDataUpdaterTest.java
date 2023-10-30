package org.identifiers.cloud.ws.resolver.daemons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resolver.RedisTestServer;
import org.identifiers.cloud.ws.resolver.daemons.models.HqServiceResponseGetResolverDataset;
import org.identifiers.cloud.ws.resolver.daemons.models.ResolverDatasetPayload;
import org.identifiers.cloud.ws.resolver.daemons.models.ServiceResponse;
import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = RedisTestServer.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ResolverDataUpdaterTest {

    @Autowired
    NamespaceRespository namespaceRespository;

    @Autowired
    ResolverDataUpdater updater;

    @Autowired
    RestTemplate restTemplate;

    @Value("${org.identifiers.cloud.ws.resolver.data.source.url}")
    private String resolverDataDumpWsEndpoint;

    final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void stopUpdaterThread() {
        updater.setShutdown();
    }

    @Before
    public void setupRegistryServer() throws JsonProcessingException {
        List<Namespace> mockedNamespaces = Collections.singletonList(
                new Namespace().setPrefix("mock_prefix").setPattern("\\d+").setName("Mocked Prefix")
                        .setResources(Collections.singletonList(new Resource()
                                .setName("Mocked Resource").setSampleId("123")
                                .setUrlPattern("https://google.com?q={$id}"))));

        ServiceResponse<ResolverDatasetPayload> response = new HqServiceResponseGetResolverDataset()
                .setApiVersion("1.0")
                .setPayload(new ResolverDatasetPayload().setNamespaces(mockedNamespaces));
        String responseStr = mapper.writeValueAsString(response);

        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        server.expect(manyTimes(), requestTo(resolverDataDumpWsEndpoint))
                .andRespond(withStatus(HttpStatus.OK).body(responseStr).contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateNamespace() { //FIXME: this only works when run by itself. Probably move redis server to test configuration
        updater.updateNamespaces();
        assertEquals("Only find one namespace", 1, namespaceRespository.count());
        assertNotNull("Only find mocked prefix namespace", namespaceRespository.findByPrefix("mock_prefix"));
        assertEquals("Only one resource", 1, namespaceRespository.findByPrefix("mock_prefix").getResources().size());
    }
}