package org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults;

import org.identifiers.cloud.ws.linkchecker.channels.Publisher;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults
 * Timestamp: 2018-08-02 12:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Publishers announces link check results on Redis
 */
@Component
public class LinkCheckResultsPublisher extends Publisher<String, LinkCheckResult> {
    @Autowired
    private RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate;

    // TODO - Refactor this to outsource publishing things on a channel
    @Autowired
    private ChannelTopic channelLinkCheckResults;

    @Override
    protected ChannelTopic getChannelTopic() {
        return channelLinkCheckResults;
    }

    @Override
    protected RedisTemplate<String, LinkCheckResult> getRedisTemplate() {
        return linkCheckResultRedisTemplate;
    }
}
