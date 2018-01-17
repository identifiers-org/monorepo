package org.identifiers.org.cloud.ws.resolver.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.embedded
 * Timestamp: 2018-01-17 11:28
 * ---
 */
@Component
public class RedisService {
    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        logger.info("--- STARTING Embedded REDIS ---");
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        logger.info("--- STOPPING Embedded REDIS ---");
        redisServer.stop();
    }

    public void ping() {
        // do nothing
    }
}
