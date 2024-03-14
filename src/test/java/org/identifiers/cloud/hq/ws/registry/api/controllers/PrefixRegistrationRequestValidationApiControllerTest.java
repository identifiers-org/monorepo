package org.identifiers.cloud.hq.ws.registry.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
class PrefixRegistrationRequestValidationApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {

    }

    @Test
    void controllerLoads() {
        assertNotNull(mvc);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void checkValidateProviderHomeUrlEndpoint() throws Exception {
//        ServiceRequestRegisterPrefixPayload prefixRequestPayload = new ServiceRequestRegisterPrefixPayload();
//        prefixRequestPayload.setProviderHomeUrl("http://www.google.com");
//        ServiceRequestRegisterPrefixValidate prefixRequest = new ServiceRequestRegisterPrefixValidate();
//        prefixRequest.setPayload(prefixRequestPayload);
//
//        mvc.perform(MockMvcRequestBuilders
//                .post("/prefixRegistrationApi/validateProviderHomeUrl")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(asJsonString(prefixRequest)))
//            .andDo(print())
//            .andExpect(status().isOk());
//    }

    //    @Test
//    public void testValidateProviderHomeUrl() {
//    }
//
//    @Test
//    public void testValidateInstitutionHomeUrl() {
//    }
//
//    @Test
//    public void testValidateProviderUrlPattern() {
//    }
}