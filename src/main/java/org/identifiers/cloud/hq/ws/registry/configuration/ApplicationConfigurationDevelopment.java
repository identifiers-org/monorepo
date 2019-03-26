package org.identifiers.cloud.hq.ws.registry.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.configuration
 * Timestamp: 2019-03-26 12:25
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the configuration active for development
 */
@Configuration
@Profile({"development", "standalone"})
public class ApplicationConfigurationDevelopment {
}
