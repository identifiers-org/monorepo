package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-09-18 17:28
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("Prototype")
public class MetadataFetcherChromeEngineBased implements MetadataFetcher {
    @Override
    public Object fetchMetadataFor(String url) throws MetadataFetcherException {
        return null;
    }
}
