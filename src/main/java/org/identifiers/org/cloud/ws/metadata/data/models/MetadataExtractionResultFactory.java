package org.identifiers.org.cloud.ws.metadata.data.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.data.models
 * Timestamp: 2018-09-17 10:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class MetadataExtractionResultFactory {
    private static final Logger logger = LoggerFactory.getLogger(MetadataExtractionResultFactory.class);

    private static Long ttlResultWithMetadata = 10L;
    private static Long ttlResultWithoutMetadata = 10L;

    // Factory methods
    public static MetadataExtractionResult createResultWithMetadata() {
        // TODO
    }

    public static MetadataExtractionResult createResultWithoutMetadata() {
        // TODO
    }
}
