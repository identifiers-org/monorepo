package org.identifiers.cloud.ws.resolver.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 15:11
 * ---
 *
 * Unit tests for Compact ID parsing
 */
@RunWith(Parameterized.class)
public class CompactIdTest {
    private String compactId;
    private String expectedPrefix;
    private String expectedId;
    private String testDescription;

    public CompactIdTest(String compactId, String expectedPrefix, String expectedId, String testDescription) {
        this.compactId = compactId;
        this.expectedPrefix = expectedPrefix;
        this.expectedId = expectedId;
        this.testDescription = testDescription;
    }

    @Test
    public void wellFormedCompactId() {
        CompactId compactId = new CompactId(this.compactId);
        assertThat(testDescription + " - prefix is detected correctly",
                (compactId.getPrefix() == null ? expectedPrefix == null : compactId.getPrefix().equals(expectedPrefix)),
                is(true));
        assertThat(testDescription + " - ID is detected correctly",
                (compactId.getId() == null ? expectedId == null : compactId.getId().equals(expectedId)),
                is(true));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestingValues() {
        return Arrays.asList(new Object[][]{
                // Normal use case
                {"GO:9876345", "go", "9876345", "Normal use case for Compact ID"},
                // No prefix use case
                {"Q9876345", null, "Q9876345", "Compact ID with no prefix"},
                // Multiple ':' use case
                {"prefix:IDpart1:IDpart2:Q9876345", "prefix", "IDpart1:IDpart2:Q9876345", "Compact ID using ':' within the ID part"}
        });
    }
}