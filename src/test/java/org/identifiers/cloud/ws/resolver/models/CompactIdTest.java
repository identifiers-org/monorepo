package org.identifiers.cloud.ws.resolver.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 15:11
 * ---
 *
 * Unit tests for Compact ID parsing
 */
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