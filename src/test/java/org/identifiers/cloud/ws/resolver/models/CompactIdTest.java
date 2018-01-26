package org.identifiers.cloud.ws.resolver.models;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

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

    public void wellFormedCompactId() {
        //assertThat()
        // TODO
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestingValues() {
        return Arrays.asList(new Object[][]{
                // Normal use case
                {"GO:9876345", "GO", "9876345", "Normal use case for Compact ID"},
                // No prefix use case
                {"Q9876345", null, "Q9876345", "Compact ID with no prefix"},
                // Multiple ':' use case
                {"prefix:IDpart1:IDpart2:Q9876345", "prefix", "IDpart1:IDpart2:Q9876345", "Compact ID using ':' within the ID part"}
        });
    }
}