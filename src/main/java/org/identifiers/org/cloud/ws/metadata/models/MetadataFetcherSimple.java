package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-09 16:31
 * ---
 */
@Component
@Scope("prototype")
public class MetadataFetcherSimple implements MetadataFetcher {
    @Override
    public String fetchMetadataFor(String url) throws MetadataFetcherException {
        return null;
    }
}
