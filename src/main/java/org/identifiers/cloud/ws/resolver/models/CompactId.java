package org.identifiers.cloud.ws.resolver.models;

import com.sun.javafx.binding.StringFormatter;

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
    private String prefix = null;
    private String id = null;

    // Helper
    private void workoutPrefixAndId(String compactId) throws CompactIdException {
        String[] compactIdParts = compactId.split(COMPACT_ID_PREFIX_AND_ID_SEPARATOR);
        if (compactIdParts.length > 2) {
            throw new CompactIdException(StringFormatter.format("Invalid compact ID '{}'", compactId).getValue());
        }
        int index = 0;
        if (compactIdParts.length == 2) {
            prefix = compactIdParts[index++];
        }
        id = compactIdParts[index];
    }

    public CompactId(String original) {
        this.original = original;
        // Workout the prefix and ID parts
        workoutPrefixAndId(original);
    }
}
