package org.identifiers.cloud.hq.ws.miridcontroller;

import org.identifiers.cloud.hq.ws.miridcontroller.configuration.RedisConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.PreDestroy;

@TestConfiguration
@SuppressWarnings("resource")
public class TestRedisServer {
    static GenericContainer<?> redis;

    @Autowired
    RedisConfiguration commonConfiguration;

    @Bean
    @Primary
    public RedissonClient testRedissonClient() {
        DockerImageName img = DockerImageName.parse("redis:7.2-alpine");
        redis = new GenericContainer<>(img)
                .withExposedPorts(6379)
                .withReuse(true);
        redis.start();
        return commonConfiguration.redissonClient(redis.getHost(), redis.getFirstMappedPort());
    }

    @PreDestroy
    void stopServer() {
        redis.stop();
    }
}
