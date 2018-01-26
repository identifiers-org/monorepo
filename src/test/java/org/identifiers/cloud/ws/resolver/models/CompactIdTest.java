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

    public CompactIdTest(String compactId, String expectedPrefix, String expectedId) {
        this.compactId = compactId;
        this.expectedPrefix = expectedPrefix;
        this.expectedId = expectedId;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestingValues() {
        return Arrays.asList(new Object[][]{
                // Normal use case
                {"GO:9876345", "GO", "9876345"},
                // No prefix use case
                {"Q9876345", null, "Q9876345"},
                // Multiple ':' use case
                {"prefix:IDpart1:IDpart2:Q9876345", "prefix", "IDpart1:IDpart2:Q9876345"}
        });
    }
}