package org.identifiers.cloud.ws.linkchecker.channels;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels
 * Timestamp: 2018-08-02 12:38
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This abstract base class implements the base logic for publishing on Redis channels.
 */
public abstract class Publisher<K, V> {
    protected abstract ChannelTopic getChannelTopic();
    protected abstract RedisTemplate<K, V> getRedisTemplate();
    
}
