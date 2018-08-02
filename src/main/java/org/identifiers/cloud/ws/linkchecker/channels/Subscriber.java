package org.identifiers.cloud.ws.linkchecker.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels
 * Timestamp: 2018-08-02 13:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Subscriber for announcements on a Redis backend
 */
public abstract class Subscriber<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    
    protected abstract RedisMessageListenerContainer getRedisContainer();
    protected abstract ChannelTopic getChannelTopic();
    protected abstract RedisTemplate<K, V> getRedisTemplate();

    protected void doRegisterListener() {

    }
    // TODO
}
