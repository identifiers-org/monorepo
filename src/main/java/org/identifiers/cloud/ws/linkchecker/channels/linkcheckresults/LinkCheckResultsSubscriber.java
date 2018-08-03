package org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults;

import org.identifiers.cloud.ws.linkchecker.channels.Subscriber;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults
 * Timestamp: 2018-08-02 14:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class LinkCheckResultsSubscriber extends Subscriber<String, LinkCheckResult> {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultsSubscriber.class);

    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private ChannelTopic channelTopicLinkCheckResults;

    @Autowired
    private RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate;

    @PostConstruct
    public void registerSubscriber() {
        doRegisterSubscriber();
    }

    @Override
    protected RedisMessageListenerContainer getRedisContainer() {
        return redisContainer;
    }

    @Override
    protected ChannelTopic getChannelTopic() {
        return channelTopicLinkCheckResults;
    }

    @Override
    protected RedisTemplate<String, LinkCheckResult> getRedisTemplate() {
        return linkCheckResultRedisTemplate;
    }
}
