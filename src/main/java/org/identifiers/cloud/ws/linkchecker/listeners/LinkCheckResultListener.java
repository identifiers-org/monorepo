package org.identifiers.cloud.ws.linkchecker.listeners;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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
public class LinkCheckResultListener implements MessageListener {
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
        logger.info("[REGISTER] for topic '{}'", channelKeyLinkCheckResults.getTopic());
        redisContainer.addMessageListener(this, channelKeyLinkCheckResults);
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // TODO

    }
}
