package org.identifiers.org.cloud.ws.metadata.channels.metadataExtractionResult;

import org.identifiers.org.cloud.ws.metadata.channels.Subscriber;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.channels.metadataExtractionResult
 * Timestamp: 2018-09-17 11:01
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class MetadataExtractionResultSubscriber extends Subscriber<String, MetadataExtractionResult> {
    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private ChannelTopic channelTopicMetadataExtractionResult;

    @Autowired
    private RedisTemplate<String, MetadataExtractionResult> metadataExtractionResultRedisTemplate;

}
