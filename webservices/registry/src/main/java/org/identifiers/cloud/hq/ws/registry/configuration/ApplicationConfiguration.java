package org.identifiers.cloud.hq.ws.registry.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.configuration
 * Timestamp: 2018-10-11 20:58
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@EnableJpaAuditing
@EnableRetry
@EnableScheduling
public class ApplicationConfiguration {}
