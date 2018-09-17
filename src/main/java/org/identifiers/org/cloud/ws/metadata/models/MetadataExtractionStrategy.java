package org.identifiers.org.cloud.ws.metadata.models;

import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResult;

import java.util.List;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-09-17 10:05
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface MetadataExtractionStrategy {
    MetadataExtractionResult extractMetadata(List<ResolvedResource> resolvedResources) throws MetadataExtractionStrategyException;
}
