package org.identifiers.cloud.ws.linkchecker.workout;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;

import java.util.Deque;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.configuration
 * Timestamp: 2018-05-30 14:09
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
//@Configuration
//@PropertySource("classpath:application.properties")
//@EnableRedisRepositories
public class TestApplicationConfig {
    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.queue.key.linkcheckrequests}")
    private String queueKeyLinkCheckRequests;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.channel.key.linkcheckresults}")
    private String channelKeyLinkCheckResults;

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
    public RedisTemplate<String, LinkCheckRequest> linkCheckRequestRedisTemplate() {
        RedisTemplate<String, LinkCheckRequest> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public Deque<LinkCheckRequest> linkCheckRequestQueue() {
        RedisList<LinkCheckRequest> linkCheckRequests = new DefaultRedisList<LinkCheckRequest>(queueKeyLinkCheckRequests,
                linkCheckRequestRedisTemplate());
        return linkCheckRequests;
    }

    // Publisher Subscriber
    @Bean
    public RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate() {
        RedisTemplate<String, LinkCheckResult> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

    @Bean
    public ChannelTopic channelKeyLinkCheckResults() {
        return new ChannelTopic(channelKeyLinkCheckResults);
    }
}