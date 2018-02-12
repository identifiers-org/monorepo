package org.identifiers.org.cloud.ws.metadata.models;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(MetadataFetcherSimple.class);

    @Override
    public String fetchMetadataFor(String url) throws MetadataFetcherException {
        // TODO - Fetch URL content
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new MetadataFetcherException(String.format("METADATA FETCH ERROR for URL '%s', there was a problem while fetching its content", url));
        }
        logger.debug("Retrive content for URL '{}', titled '{}'", url, document.title());
        // TODO - Look for JSON-LD
        String jsonldSelector = "script[type='application/ld+json'";
        Elements jsonldElements = document.head().select(jsonldSelector);
        if (jsonldElements.size() > 1) {
            String errorMessage = String.format("MULTIPLE JSON-LD entries found in the header of URL '%s', entries: %s", url, jsonldElements.toString());
            logger.error(errorMessage);
            throw new MetadataFetcherException(errorMessage);
        }
        // TODO - Check on schema.org context
        return null;
    }
}
