package org.identifiers.org.cloud.ws.metadata.models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Scope("prototype")
public class MetadataFetcherChromeEngineBased implements MetadataFetcher {
    private static final Logger logger = LoggerFactory.getLogger(MetadataFetcherChromeEngineBased.class);

    @Override
    public Object fetchMetadataFor(String url) throws MetadataFetcherException {
        WebDriver driver = new ChromeDriver();
        return null;
    }
}
