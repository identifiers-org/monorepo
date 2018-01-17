package org.identifiers.org.cloud.ws.resolver.daemons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.daemons
 * Timestamp: 2018-01-16 16:34
 * ---
 */
public class ResolverDataUpdater extends Thread {
    private Logger logger = LoggerFactory.getLogger(ResolverDataUpdater.class);

    @Value("${spring.redis.port}")
    private int redisPort;

}
