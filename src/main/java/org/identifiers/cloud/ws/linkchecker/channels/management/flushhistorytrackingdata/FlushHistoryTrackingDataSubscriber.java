package org.identifiers.cloud.ws.linkchecker.channels.management.flushhistorytrackingdata;

import org.identifiers.cloud.ws.linkchecker.channels.Subscriber;
import org.identifiers.cloud.ws.linkchecker.data.models.FlushHistoryTrackingDataMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.management.flushhistorytrackingdata
 * Timestamp: 2018-08-03 11:19
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class FlushHistoryTrackingDataSubscriber extends Subscriber<String, FlushHistoryTrackingDataMessage> {
    @Autowired
    private RedisMessageListenerContainer redisContainer;
}
