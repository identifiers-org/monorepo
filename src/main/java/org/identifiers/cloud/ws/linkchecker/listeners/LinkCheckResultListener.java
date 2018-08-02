package org.identifiers.cloud.ws.linkchecker.listeners;

import org.identifiers.cloud.ws.linkchecker.channels.Subscriber;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingService;
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
 * Package: org.identifiers.cloud.ws.linkchecker.listeners
 * Timestamp: 2018-05-29 16:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This component listens to the link check results channel for announcements.
 */
@Component
public class LinkCheckResultListener extends Subscriber<String, LinkCheckResult> {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultListener.class);

    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private ChannelTopic channelKeyLinkCheckResults;

    @Autowired
    private RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate;

    @Autowired
    private HistoryTrackingService historyTrackingService;

    @PostConstruct
    public void registerListener() {
        doRegisterSubscriber();
    }

    @Override
    protected RedisMessageListenerContainer getRedisContainer() {
        return redisContainer;
    }

    @Override
    protected ChannelTopic getChannelTopic() {
        return channelKeyLinkCheckResults;
    }

    @Override
    protected RedisTemplate<String, LinkCheckResult> getRedisTemplate() {
        return linkCheckResultRedisTemplate;
    }

    @Override
    protected void processValue(LinkCheckResult value) {
        logger.info("Processing link check result announcement for URL '{}', provider ID '{}', resource ID '{}'",
                value.getUrl(),
                value.getProviderId(),
                value.getResourceId());
        historyTrackingService.updateTrackerWith(value);
    }
}
