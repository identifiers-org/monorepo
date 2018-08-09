package org.identifiers.cloud.ws.linkchecker.configuration;

import org.identifiers.cloud.ws.linkchecker.data.models.FlushHistoryTrackingDataMessage;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;

import java.util.concurrent.BlockingDeque;

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
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.queue.key.linkcheckrequests}")
    private String queueKeyLinkCheckRequests;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.channel.key.linkcheckresults}")
    private String channelKeyLinkCheckResults;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.channel.key.flushhistorytrackingdata}")
    private String channelKeyFlushHistoryTrackingData;

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
    public BlockingDeque<LinkCheckRequest> linkCheckRequestQueue() {
        RedisList<LinkCheckRequest> linkCheckRequests = new DefaultRedisList<>(queueKeyLinkCheckRequests,
                linkCheckRequestRedisTemplate());
        return linkCheckRequests;
    }

    // Publisher Subscriber
    // Link Check Results
    @Bean
    public RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate() {
        RedisTemplate<String, LinkCheckResult> redisTemplate = new RedisTemplate<>();
        // TODO - Refactor this part of the code when possible
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    // Flush History Tracking Data Message
    @Bean
    public RedisTemplate<String, FlushHistoryTrackingDataMessage> flushHistoryTrackingDataMessageRedisTemplate() {
        RedisTemplate<String, FlushHistoryTrackingDataMessage> redisTemplate = new RedisTemplate<>();
        // TODO - Refactor this part of the code when possible
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    // Redis Container
    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.start();
        return container;
    }

    // Channels
    // Link Check Results Channel
    @Bean
    public ChannelTopic channelTopicLinkCheckResults() {
        return new ChannelTopic(channelKeyLinkCheckResults);
    }

    @Bean
    public ChannelTopic channelTopicFlushHistoryTrackingData() {
        return new ChannelTopic(channelKeyFlushHistoryTrackingData);
    }
}
