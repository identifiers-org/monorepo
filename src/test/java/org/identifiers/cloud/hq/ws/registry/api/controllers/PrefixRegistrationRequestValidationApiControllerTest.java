package org.identifiers.cloud.hq.ws.registry.api.controllers;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PrefixRegistrationRequestRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PrefixRegistrationSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PrefixRegistrationRequestValidationApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    PrefixRegistrationRequestRepository requestRepository;
    @Autowired
    PrefixRegistrationSessionRepository sessionRepository;

    Resource testPrefixRequest = new ClassPathResource("samplePrefixRequest.json");

    @RegisterExtension
    static GreenMailExtension mailServer = new GreenMailExtension(ServerSetupTest.SMTP);

    @BeforeEach
    void setUserPassword(@Value("${spring.mail.username}")
                         String username,
                         @Value("${spring.mail.password}")
                         String password) {
        mailServer.setUser(username, password);
    }

    @Test
    void contextLoads() {

    }

    @Test
    void controllerLoads() {
        assertNotNull(mvc);
    }

    @Test
    void checkRegisterNamespace() throws Exception {
        mvc.perform(post("/prefixRegistrationApi/registerPrefix")
                        .content(testPrefixRequest.getContentAsByteArray())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Two emails, one for the requester and one for the curators.
        // Each email is two messages because each has a TO and a CC
        assertEquals(4, mailServer.getReceivedMessages().length);

        assertEquals(1, requestRepository.count());
        assertEquals(1, sessionRepository.count());
        assertFalse(sessionRepository.findAll().get(0).isClosed());
    }
}