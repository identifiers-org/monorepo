package org.identifiers.org.cloud.ws.resolver.configuration;

import org.identifiers.org.cloud.ws.resolver.embedded.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.configuration
 * Timestamp: 2018-01-17 11:36
 * ---
 */
@Configuration
public class ProductionConfiguration {

    @Autowired
    private RedisService redisService;

}
