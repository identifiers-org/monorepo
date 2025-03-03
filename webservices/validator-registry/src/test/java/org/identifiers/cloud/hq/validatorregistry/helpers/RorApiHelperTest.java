package org.identifiers.cloud.hq.validatorregistry.helpers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        RorApiHelperTest.TestConfig.class,
        RorApiHelper.class
})
class RorApiHelperTest {
    @Autowired
    RorApiHelper rorApiHelper;

    @Test
    void fetchRorInfoFor() {
        String ebiRorId = "https://ror.org/02catss52";

        var rorEbiInfoOpt = rorApiHelper.fetchRorInfoFor(ebiRorId);
        assertNotNull(rorEbiInfoOpt);
        assertTrue(rorEbiInfoOpt.isPresent());

        var info = rorEbiInfoOpt.get();
        assertEquals(ebiRorId, info.id());
        assertEquals("European Bioinformatics Institute", info.name());
        assertFalse(info.links().isEmpty());
        assertNotNull(info.country());
        assertEquals("United Kingdom", info.country().name());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Test
    void queryRorApiInformation() {
        String query = "European Bioinformatics";
        var results = rorApiHelper.query(query);

        assertFalse(results.isEmpty());

    }
}