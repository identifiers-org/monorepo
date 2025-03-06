package org.identifiers.cloud.hq.validatorregistry.helpers;

import org.identifiers.cloud.hq.validatorregistry.configurations.ApplicationConfiguration;
import org.identifiers.cloud.hq.validatorregistry.helpers.WikiDataHelper.WikiDataOrganizationDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {WikiDataHelperTest.TestConfig.class, ApplicationConfiguration.class, WikiDataHelper.class})
class WikiDataHelperTest {
    public static final String EBI_ROR_ID = "02catss52";
    public static final String EBI_WIKIDATA_ID = "Q1341845";

    @Autowired
    WikiDataHelper wikiDataHelper;

    @Test
    void testSearchForWikiDataIds() {
        final String ebiName = "European Bioinformatics Institute";
        var results = wikiDataHelper.searchForWikiDataIds(ebiName);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(EBI_WIKIDATA_ID::equals));
    }

    @Test
    void testGetOrganizationDetailsFromQids() throws IOException {
        var testIds = List.of(EBI_WIKIDATA_ID, "Q11346475", "Q622664", "Q41506", "Q11713409", "Q1133630");
        var results = wikiDataHelper.getOrganizationDetailsFromQids(testIds);
        assertNotNull(results);
        assertEquals(6, results.size());
        assertTrue(results.stream()
                .map(WikiDataOrganizationDetails::getRorId)
                .anyMatch(EBI_ROR_ID::equals));
    }


    @Test
    void testNameFinding() {
        var name = "Ontario Institute for Cancer Research, NYU Medical School, Cold Spring Harbor Laboratory and European Bioinformatics Institute";
        var orgNames = wikiDataHelper.findOrganizationNamesUsingNlp(name);
        assertNotNull(orgNames);
        assertTrue(orgNames.length > 0);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"02catss52", "http://ror.org/02catss52", "https://ror.org/02catss52"})
    void testGetOrganizationDetailsFromRorId(String rorId) {
        var orgDetails = wikiDataHelper.getOrganizationDetailsFromRorId(rorId);
        assertNotNull(orgDetails);
        assertTrue(orgDetails.isPresent());
        assertEquals(EBI_ROR_ID, orgDetails.get().getRorId());
        assertEquals("European Bioinformatics Institute", orgDetails.get().getName());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }
}