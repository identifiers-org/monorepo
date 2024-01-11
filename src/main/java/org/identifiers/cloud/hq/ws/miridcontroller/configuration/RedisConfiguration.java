package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
public class RedisConfiguration {
    @Bean
    public RedissonClient redissonClient(
            @Value("${spring.redis.host}")
            String redisHost,
            @Value("${spring.redis.port}")
            int redisPort
    ) {
        Config config = new Config();
        config.useSingleServer().setAddress(String.format("redis://%s:%d", redisHost, redisPort));
        return Redisson.create(config);
    }
}
