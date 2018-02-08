package org.identifiers.org.cloud.ws.metadata.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-08 11:54
 * ---
 */
public interface MetadataFetcher {

    String fetchMetadataFor(String url);
}
