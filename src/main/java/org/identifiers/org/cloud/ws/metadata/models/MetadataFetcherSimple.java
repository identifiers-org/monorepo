package org.identifiers.org.cloud.ws.metadata.models;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        // TODO - Fetch URL content
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new MetadataFetcherException(String.format("METADATA FETCH ERROR for URL '%s', there was a problem while fetching its content", url));
        }
        // TODO - Look for JSON-LD
        // TODO - Check on schema.org context
        return null;
    }
}
