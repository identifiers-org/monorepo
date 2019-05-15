package org.identifiers.satellite.frontend.satellitewebspa.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.models
 * Timestamp: 2019-05-15 13:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This object defines the configuration information for the Web SPA
 */
@Component
public class SpaConfiguration {
    
    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.resolver.url}")
    private String urlResolver;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.hqregistry.url}")
    private String urlHqRegistry;
}
