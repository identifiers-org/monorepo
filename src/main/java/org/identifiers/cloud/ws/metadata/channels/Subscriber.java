package org.identifiers.cloud.ws.metadata.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.channels
 * Timestamp: 2018-09-17 10:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public abstract class Subscriber<K, V> implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    private final Set<Listener<V>> listeners = new HashSet<>();

    protected abstract RedisMessageListenerContainer getRedisContainer();
    protected abstract ChannelTopic getChannelTopic();
    protected abstract RedisTemplate<K, V> getRedisTemplate();

    protected void doRegisterSubscriber() {
        logger.info("[REGISTER] for topic '{}'", getChannelTopic().getTopic());
        getRedisContainer().addMessageListener(this, getChannelTopic());
    }

    public void addListener(Listener<V> listener) {
        listeners.add(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(Message message, byte[] bytes) {
        V value = (V) getRedisTemplate().getValueSerializer().deserialize(message.getBody());
        listeners.parallelStream().forEach(listener -> listener.process(value));
    }
}
