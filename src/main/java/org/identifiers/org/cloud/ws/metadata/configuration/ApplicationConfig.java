package org.identifiers.org.cloud.ws.metadata.configuration;

import org.springframework.context.annotation.Configuration;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.configuration
 * Timestamp: 2018-02-07 11:46
 * ---
 */
@Configuration
public class ApplicationConfig {
    private String redisHost;
    private int redisPort;
    private String queueKeyMetadataExtractionRequest;
    private String channelKeyMetadataExtractionResult;
}
