package org.identifiers.cloud.ws.resolver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.configuration
 * Timestamp: 2018-03-17 5:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Profile("production")
public class ProductionConfiguration {
}
