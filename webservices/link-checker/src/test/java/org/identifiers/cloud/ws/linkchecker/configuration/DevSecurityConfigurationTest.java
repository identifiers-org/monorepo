package org.identifiers.cloud.ws.linkchecker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.ws.linkchecker.TestRedisServer;
import org.identifiers.cloud.ws.linkchecker.api.models.LinkScoringApiModel;
import org.identifiers.cloud.ws.linkchecker.api.models.ManagementApiModel;
import org.identifiers.cloud.commons.messages.responses.linkchecker.*;
import org.identifiers.cloud.commons.messages.requests.linkchecker.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TestRedisServer.class})
class DevSecurityConfigurationTest {
    @MockBean
    LinkScoringApiModel linkScoringApiModel;

    @MockBean
    ManagementApiModel managementApiModel;

    MockMvc mvc;
    static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setupMock(@Autowired WebApplicationContext context) {
        var response = new ServiceResponse<ServiceResponseScoringRequestPayload>();
        doReturn(response).when(linkScoringApiModel).getScoreForResolvedId(any());
        doReturn(response).when(linkScoringApiModel).getScoreForProvider(any());
        doCallRealMethod().when(linkScoringApiModel).getScoreForUrl(any());

        var mngModel = new ServiceResponse<ServiceResponseManagementRequestPayload>();
        doReturn(mngModel).when(managementApiModel).flushLinkCheckingHistory();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetScoreForUrl() throws Exception {
        ScoringRequestPayload payload = new ScoringRequestPayload();
        payload.setUrl("google.com").setAccept401or403(false);
        var request = ServiceRequest.of(payload);

        mvc.perform(post("/getScoreForUrl")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Not implemented
    }

    @Test
    void testGetScoreForResolvedId() throws Exception {
        ScoringRequestWithIdPayload payload = new ScoringRequestWithIdPayload().setId("1");
        payload.setUrl("google.com").setAccept401or403(false);
        var request = ServiceRequest.of(payload);

        mvc.perform(post("/getScoreForResolvedId")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetScoreForProvider() throws Exception {
        var payload = new ScoringRequestWithIdPayload()
                                .setId("1")
                                .setAccept401or403(false)
                                .setUrl("google.com");
        var request = ServiceRequest.of(payload);

        mvc.perform(post("/getScoreForProvider")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testFlushLinkCheckingHistory() throws Exception {
        mvc.perform(get("/management/flushLinkCheckingHistory"))
                .andExpect(status().isOk());
        verify(managementApiModel, atMostOnce()).flushLinkCheckingHistory();
    }

    @Test
    void testActuators() throws Exception {
        mvc.perform(get("/actuator"))
                .andExpect(status().isOk());
    }

    @Test
    void testActuatorsLoggers() throws Exception {
        mvc.perform(get("/actuator/loggers"))
                .andExpect(status().isOk());
    }

    @Test
    void testActuatorsHealth() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}