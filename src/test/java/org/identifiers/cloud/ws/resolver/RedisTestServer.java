package org.identifiers.cloud.ws.resolver;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class RedisTestServer {
    GenericContainer<?> redisServer;

    @PostConstruct
    public void SetupRedisServer() {
        redisServer = new GenericContainer<>("redis").withExposedPorts(6379);
        redisServer.start();
        System.setProperty("spring.redis.port", String.valueOf(redisServer.getMappedPort(6379)));
    }

    @PreDestroy
    public void StopRedisServer() {
        if (redisServer.isRunning()) {
            redisServer.stop();
        }
    }
}
