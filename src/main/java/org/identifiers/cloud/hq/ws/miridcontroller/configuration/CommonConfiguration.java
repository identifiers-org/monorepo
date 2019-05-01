package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.configuration
 * Timestamp: 2019-02-19 14:09
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Common configuration
 */
@Configuration
@EnableJpaAuditing
@EnableRetry
public class CommonConfiguration {
    // TODO
    // Redis configuration
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
}
