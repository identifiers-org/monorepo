package org.identifiers.org.cloud.ws.resolver.embedded;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.embedded
 * Timestamp: 2018-01-17 11:28
 * ---
 */
@Component
public class RedisService {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;
}
