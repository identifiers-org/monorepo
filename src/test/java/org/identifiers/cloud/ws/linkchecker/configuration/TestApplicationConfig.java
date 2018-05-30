package org.identifiers.cloud.ws.linkchecker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.configuration
 * Timestamp: 2018-05-30 11:45
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Import({ApplicationConfig.class})
@Profile("test")
public class TestApplicationConfig {
}
