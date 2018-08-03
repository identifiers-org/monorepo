package org.identifiers.cloud.ws.linkchecker.channels.management.flushhistorytrackingdata;

import org.identifiers.cloud.ws.linkchecker.channels.Subscriber;
import org.identifiers.cloud.ws.linkchecker.data.models.FlushHistoryTrackingDataMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.management.flushhistorytrackingdata
 * Timestamp: 2018-08-03 11:19
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class FlushHistoryTrackingDataSubscriber extends Subscriber<String, FlushHistoryTrackingDataMessage> {
    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private ChannelTopic channelTopicFlushHistoryTrackingData;

    @Autowired
    private RedisTemplate<String, FlushHistoryTrackingDataMessage> flushHistoryTrackingDataMessageRedisTemplate;

}
