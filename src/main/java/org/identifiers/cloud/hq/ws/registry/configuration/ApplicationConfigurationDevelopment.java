package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
@Slf4j
public class ApplicationConfigurationDevelopment implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("CORS configuration for DEVELOPMENT");
        registry.addMapping("/**").allowedMethods("*");

        // Add more mappings...
    }
}
