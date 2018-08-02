package org.identifiers.cloud.ws.linkchecker.daemons;

import org.identifiers.cloud.ws.linkchecker.data.LinkCheckModelsHelper;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.data.services.LinkCheckResultService;
import org.identifiers.cloud.ws.linkchecker.data.services.LinkCheckResultServiceException;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerException;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Deque;
import java.util.Random;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.daemons
 * Timestamp: 2018-05-29 10:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This daemon pulls link checking requests from a queue, runs them through a link checker, and lodges in the results.
 */
@Component
public class LinkChecker extends Thread {
    private static final int WAIT_TIME_LIMIT_SECONDS = 30;
    private static final Logger logger = LoggerFactory.getLogger(LinkChecker.class);

    private boolean shutdown = false;

    @Autowired
    private Deque<LinkCheckRequest> linkCheckRequestQueue;

    // It may not be the best of the names... I may refactor this in the future
    @Autowired
    private org.identifiers.cloud.ws.linkchecker.strategies.LinkChecker linkCheckingStrategy;

    @Autowired
    private LinkCheckResultService linkCheckResultService;

    @Autowired
    private RedisTemplate<String, LinkCheckResult> linkCheckResultRedisTemplate;

    // TODO - Refactor this to outsource publishing things on a channel
    @Autowired
    private ChannelTopic channelLinkCheckResults;

    private LinkCheckResult attendLinkCheckRequest(LinkCheckRequest linkCheckRequest) {
        // Check URL
        LinkCheckerReport linkCheckerReport;
        try {
            linkCheckerReport = linkCheckingStrategy.check(linkCheckRequest.getUrl());
        } catch (LinkCheckerException e) {
            logger.error("Could not attend link checking request for URL '{}', reason '{}'", linkCheckRequest.getUrl()
                    , e.getMessage());
            return null;
        }
        // Log the results
        logger.info("Link Check result for URL '{}', HTTP Status '{}', assessment '{}'",
                linkCheckerReport.getUrl(),
                linkCheckerReport.getHttpStatus(),
                linkCheckerReport.isUrlAssessmentOk() ? "OK" : "NOT OK");
        return LinkCheckModelsHelper.getResultFromReport(linkCheckerReport, linkCheckRequest);
    }

    private void randomWait() {
        Random random = new Random(System.currentTimeMillis());
        try {
            long waitTimeSeconds = random.nextInt(WAIT_TIME_LIMIT_SECONDS);
            logger.info("Waiting {}s before we check again for URLs", waitTimeSeconds);
            Thread.sleep(waitTimeSeconds * 1000);
        } catch (InterruptedException e) {
            logger.warn("The Link Checker Daemon has been interrupted while waiting for " +
                    "another iteration. Stopping the daemon, no more URL check requests will be processed");
            shutdown = true;
        }
    }

    public synchronized boolean isShutdown() {
        return shutdown;
    }

    public synchronized void setShutdown() {
        this.shutdown = true;
    }

    @Override
    public void run() {
        logger.info("--- [START] Link Checker Daemon ---");

        while (!isShutdown()) {
            // Pop element, if any, from the link checking request queue
            LinkCheckRequest linkCheckRequest = linkCheckRequestQueue.pollFirst();
            if (linkCheckRequest == null) {
                // If no element is in there, wait a random amount of time before trying again
                logger.info("No URL check request found");
                randomWait();
                continue;
            }
            // Check URL
            LinkCheckResult linkCheckResult = attendLinkCheckRequest(linkCheckRequest);
            if (linkCheckResult != null) {
                try {
                    linkCheckResultService.save(linkCheckResult);
                } catch (LinkCheckResultServiceException e) {
                    logger.error("COULD not save link check result for URL '{}'", linkCheckResult.getUrl());
                }
                // Announce the link checking results
                linkCheckResultRedisTemplate.convertAndSend(channelLinkCheckResults.getTopic(), linkCheckResult);
            }
        }
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
