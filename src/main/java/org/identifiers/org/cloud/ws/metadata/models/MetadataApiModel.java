package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@Component
public class MetadataApiModel {

    @Autowired
    private IdResolver idResolver;

    public MetadataApiResponse getMetadataFor(String compactId) {
        // TODO
        // TODO - Resolve the Compact ID
        // TODO - Select the provider
        // TODO - Extract the metadata
        // TODO - return the response
        return null;
    }
}
