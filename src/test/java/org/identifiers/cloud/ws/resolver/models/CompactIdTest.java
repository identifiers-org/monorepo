package org.identifiers.cloud.ws.resolver.models;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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


}