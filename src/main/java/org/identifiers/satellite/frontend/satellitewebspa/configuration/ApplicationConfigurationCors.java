package org.identifiers.satellite.frontend.satellitewebspa.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.configuration
 * Timestamp: 2019-05-17 12:06
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Slf4j
public class ApplicationConfigurationCors implements WebMvcConfigurer {
    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.cors.origin}")
    private String corsOrigins;

    @PostConstruct
    private void postConstruct() {
        log.info(String.format("[CONFIG] CORS, domains '%s", corsOrigins));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(corsOrigins.split(",")).allowedMethods("*");
    }
}
