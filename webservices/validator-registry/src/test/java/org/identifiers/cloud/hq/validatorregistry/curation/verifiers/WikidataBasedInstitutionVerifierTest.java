package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.Location;
import org.identifiers.cloud.hq.validatorregistry.TestApplicationConfiguration;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.WikiDataHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.WikiDataHelper.WikiDataOrganizationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "org.identifiers.cloud.verifiers.wikidata.enabled=true",
        "logging.level.org.springframework=DEBUG"
}, classes = TestApplicationConfiguration.class)
class WikidataBasedInstitutionVerifierTest {

    @Autowired
    WikidataBasedInstitutionVerifier verifier;
    @Autowired
    WikiDataHelper wikiDataHelper;

    Institution institution;
    WikiDataOrganizationDetails mockMatch;

    @BeforeEach
    void setUp() {
        mockMatch = Mockito.mock(WikiDataOrganizationDetails.class);
        when(mockMatch.getName()).thenReturn("European Bioinformatics Institute");
        when(mockMatch.getHomeUrl()).thenReturn("https://www.ebi.ac.uk/");
        when(mockMatch.getLocCode()).thenReturn("GB");
        when(mockMatch.getLocName()).thenReturn("United Kingdom");
        when(mockMatch.getRorId()).thenReturn("manycatss");
        when(mockMatch.getQid()).thenReturn("https://www.wikidata.org/wiki/Q57");

        institution = new Institution()
                .setId(2L)
                .setName("European Bioinformatics Institute")
                .setHomeUrl("https://www.ebi.ac.uk/")
                .setRorId("https://ror.org/02catss52")
                .setDescription("Description")
                .setLocation(new Location("GB", "United Kingdom"));
    }

    @Test
    void testMeasureDistance() {
        double distance = verifier.measureDistance(institution, mockMatch);

        assertTrue(distance < 0.05);
    }

    @Test
    void doValidateFromMatchWithDifferences() {
        var notification = verifier.validateFromMatch(institution, mockMatch);
        assertTrue(notification.isPresent());
        assertEquals(institution.getId(), notification.get().getTargetId());
        assertEquals(TargetEntityHelper.getEntityTypeOf(institution), notification.get().getTargetType());
    }

    @Test
    void doValidateFromMatchRorMatch() {
        var match = wikiDataHelper.getOrganizationDetailsFromRorId("02catss52");
        assertTrue(match.isPresent());

        var notification = verifier.validateFromMatch(institution, match.get());
        assertFalse(notification.isPresent());
    }
}