package org.identifiers.cloud.ws.metadata;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.PreDestroy;

@TestConfiguration
@SuppressWarnings("resource")
public class TestRedisServer {
    static final GenericContainer<?> redis;

    static {
        DockerImageName img = DockerImageName.parse("redis");
        redis = new GenericContainer<>(img)
                .withExposedPorts(6379)
                .withReuse(false);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }

    @PreDestroy
    void stopServer() {
        redis.stop();
    }
}
