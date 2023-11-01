package org.identifiers.cloud.hq.ws.registry.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PrefixRegistrationRequestValidationApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() throws Exception{}

    @Test
    public void controllerLoads() throws Exception{
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