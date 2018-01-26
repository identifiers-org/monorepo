package org.identifiers.cloud.ws.resolver.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 12:26
 * ---
 */
public class CompactId {
    public static final String COMPACT_ID_PREFIX_AND_ID_SEPARATOR = ":";

    private String original;
    private String prefix = "";
    private String id = "";

    // Helper
    private void workoutPrefixAndId(String compactId) throws CompactIdException {
        // TODO
    }

    public CompactId(String original) {
        this.original = original;
        // TODO - Workout the prefix and ID parts
    }
}
