package org.identifiers.cloud.ws.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisServer {
    GenericContainer<?> redis;

    public TestRedisServer () {
        DockerImageName img = DockerImageName.parse("redis");
        redis = new GenericContainer<>(img).withExposedPorts(6379);
    }

    @PostConstruct
    void startServer() {
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

    @PreDestroy
    void stopServer() {
        redis.stop();
    }
}
