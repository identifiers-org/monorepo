package org.identifiers.cloud.ws.linkchecker;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.PreDestroy;

@TestConfiguration
@SuppressWarnings("resource")
public class TestRedisServer {
    static GenericContainer<?> redisContainer;

    static {
        DockerImageName img = DockerImageName.parse("redis:7.4-alpine");
        redisContainer = new GenericContainer<>(img)
                .withExposedPorts(6379)
                .withReuse(true)
                .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*", 1));
        redisContainer.start();
        System.setProperty("spring.redis.host", redisContainer.getHost());
        System.setProperty("spring.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @PreDestroy
    void stopServer() {
        redisContainer.stop();
    }
}
