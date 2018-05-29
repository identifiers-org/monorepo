package org.identifiers.cloud.ws.linkchecker.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

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
    // TODO

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // TODO
    }
}
