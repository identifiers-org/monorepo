package org.identifiers.org.cloud.ws.resolver.daemons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Random;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.daemons
 * Timestamp: 2018-01-16 16:34
 * ---
 */
@Component
public class ResolverDataUpdater extends Thread {
    private static final int WAIT_TIME_LIMIT_SECONDS = 3600;
    private Logger logger = LoggerFactory.getLogger(ResolverDataUpdater.class);

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    private boolean shutdown = false;

    public boolean isShutdown() {
        return shutdown;
    }

    public void setShutdown() {
        this.shutdown = true;
    }

    @Override
    public void run() {
        logger.info("--- Resolver Data Update Daemon Start ---");
        Random random = new Random(System.currentTimeMillis());
        while (!isShutdown()) {
            // TODO - Do your stuff
            // Wait for a predefined period of time before the next announcement
            try {
                long waitTime = random.nextInt(WAIT_TIME_LIMIT_SECONDS) * 1000;
                Thread.sleep(waitTime);
                logger.info("Waiting {}s before the we check again for the resolver data", waitTime);
            } catch (InterruptedException e) {
                logger.warn("The Resolver Data Update Daemon has been interrupted while waiting for " +
                        "another iteration. Stopping the service, no more updates will be submitted");
                shutdown = true;
            }
        }
    }

    @PostConstruct
    public void autoStartThread() {
        start();
    }

    @PreDestroy
    public void stopDaemon() {
        logger.info("--- [STOPPING] Resolver Data Update Daemon ---");
        setShutdown();
    }
}
