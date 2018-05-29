package org.identifiers.cloud.ws.linkchecker.daemons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.daemons
 * Timestamp: 2018-05-29 10:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This daemon pulls link checking requests from a queue, runs them through a link checker, and lodges in the results.
 */
@Component
public class LinkChecker extends Thread {
    private static final int WAIT_TIME_LIMIT_SECONDS = 1800;
    private static final Logger logger = LoggerFactory.getLogger(LinkChecker.class);

    private boolean shutdown = false;

    public synchronized boolean isShutdown() {
        return shutdown;
    }

    public synchronized void setShutdown() {
        this.shutdown = true;
    }

    @Override
    public void run() {
        // TODO
    }

    @PostConstruct
    public void autoStartThread() {
        start();
    }

    @PreDestroy
    public void stopDaemon() {
        logger.info("--- [STOPPING] Link Checker Daemon ---");
        setShutdown();
    }
}
