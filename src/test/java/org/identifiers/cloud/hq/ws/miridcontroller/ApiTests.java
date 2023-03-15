package org.identifiers.cloud.hq.ws.miridcontroller;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("authdisabled")
public class ApiTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MirIdManagementStrategy strategy;

    @Test
    public void testMintingEndpoint() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/mirIdApi/mintId")
                                                                      .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern("MIR:\\d{8}")));
    }

    @Test
    public void testKeepAliveEndpoint() throws Exception {
        long mirid = strategy.mintId();

        String url = String.format("/mirIdApi/keepAlive/MIR:%08d", mirid);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
//                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testReturnEndpoint() throws Exception {
        long mirid = strategy.mintId();

        String url = String.format("/mirIdApi/returnId/MIR:%08d", mirid);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
//                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testLoadEndpoint() throws Exception {
        String url = String.format("/mirIdApi/loadId/MIR:%08d", 1000);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
//                .andDo(print())
                .andExpect(status().isOk());
    }
}
