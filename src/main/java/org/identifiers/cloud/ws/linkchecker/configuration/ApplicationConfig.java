package org.identifiers.cloud.ws.linkchecker.configuration;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;

import java.util.Deque;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.configuration
 * Timestamp: 2018-05-22 13:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Default Application configuration
 */
@Configuration
@EnableRedisRepositories
public class ApplicationConfig {
    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("org.identifiers.cloud.ws.linkchecker.backend.data.queue.key.linkcheckrequests")
    private String queueKeyLinkCheckRequests;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost,
                redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public Deque<LinkCheckRequest> linkCheckRequestDeque() {
        RedisTemplate<String, LinkCheckRequest> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        RedisList<LinkCheckRequest> linkCheckRequests = new DefaultRedisList<LinkCheckRequest>(queueKeyLinkCheckRequests, redisTemplate);
        return linkCheckRequests;
    }
}
