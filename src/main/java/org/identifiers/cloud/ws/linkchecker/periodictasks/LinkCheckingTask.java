package org.identifiers.cloud.ws.linkchecker.periodictasks;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.ws.linkchecker.channels.PublisherException;
import org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults.LinkCheckResultsPublisher;
import org.identifiers.cloud.ws.linkchecker.data.LinkCheckModelsHelper;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.data.services.LinkCheckResultServiceException;
import org.identifiers.cloud.ws.linkchecker.data.services.LinkCheckResultsService;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerException;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerReport;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MINUTES;

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
@RequiredArgsConstructor
@ConditionalOnProperty(value = "org.identifiers.cloud.ws.linkchecker.daemon.periodiclinkcheckingtask.enabled")
public class LinkCheckingTask implements Runnable{
    static final Logger logger = LoggerFactory.getLogger(LinkCheckingTask.class);
    static final Random random = new Random(System.currentTimeMillis());


    @Value("${org.identifiers.cloud.ws.linkchecker.daemon.linkchecker.nthreads}")
    int numberOfThreads;
    @Value("${org.identifiers.cloud.ws.linkchecker.daemon.linkchecker.waittime.min}")
    Duration minWaitTime;
    @Value("${org.identifiers.cloud.ws.linkchecker.daemon.linkchecker.waittime.max}")
    Duration waitTimeLimit;
    @Value("${org.identifiers.cloud.ws.linkchecker.daemon.linkchecker.waittime.polltimeout}")
    Duration waitTimePoolLinkCheckRequestQueue;


    final LinkCheckerStrategy linkCheckingStrategy;
    final BlockingDeque<LinkCheckRequest> linkCheckRequestQueue;
    final LinkCheckResultsService linkCheckResultsService;
    final LinkCheckResultsPublisher linkCheckResultsPublisher;
    private boolean isRunning;

    @PostConstruct
    void checkDurationParameters() {
        logger.info("Checking wait time between {} and {}. {}", minWaitTime, waitTimeLimit, minWaitTime.compareTo(waitTimeLimit));
        Assert.state(minWaitTime.compareTo(waitTimeLimit) < 0,
                "Invalid link checking configuration: Min wait time between checking task must be less than limit");

        logger.info("Using executor with {} threads", numberOfThreads);
        Assert.state(numberOfThreads > 0, "Number of threads must be a positive number");


    }

    public long getNextRandomWait() {
        return random.nextLong(minWaitTime.getSeconds(), waitTimeLimit.getSeconds()+1);
    }

    @Override
    public void run() {
        synchronized (this) {
            if (isRunning) {
                logger.info("Link checking task already running!");
                return;
            }
            isRunning = true;
        }

        final var threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler(this::handleException)
                .setNameFormat("link-chk-pool-%d")
                .build();
        final var executor = Executors.newFixedThreadPool(numberOfThreads, threadFactory);
        final var semaphore = new Semaphore(numberOfThreads);

        logger.debug("--- [START] Link Checker Task ---");
        try {
            logger.info("Polling link check request queue");
            while (true) {
                LinkCheckRequest linkCheckRequest = nextLinkCheckRequest();
                if (linkCheckRequest == null) {
                    logger.info("No URL check request found");
                    break;
                }
                semaphore.acquire();
                var future = CompletableFuture.supplyAsync(
                        () -> attendLinkCheckRequest(linkCheckRequest), executor);
                semaphore.release();
                future.thenAccept(this::attendLinkCheckResult);
            }
            executor.shutdown();
            if (!executor.awaitTermination(30, MINUTES)) {
                logger.error("Executor threads still running!");
                executor.shutdownNow();
            }
        } catch (RedisConnectionFailureException ex) {
            String msg = "Failed to connect to redis for next check request";
            if (logger.isDebugEnabled()) {
                logger.error(msg, ex);
            } else {
                logger.error("{}: {}", msg, ex.getMessage());
            }
        } catch (Exception e) {
            logger.error("An error has when acquiring next link check request", e);
            if (e instanceof InterruptedException)
                Thread.currentThread().interrupt();
        }

        synchronized (this) {
            isRunning = false;
        }
        logger.debug("--- [END] Link Checker Task ---");
    }

    private void handleException(Thread t, Throwable e) {
        if (logger.isDebugEnabled())
            logger.error("Unhandled exception in thread {}", t.getName(), e);
        else
            logger.error("Unhandled exception in thread {}: {}", t.getName(), e.getMessage());
    }

    private LinkCheckRequest nextLinkCheckRequest() {
        try {
            return linkCheckRequestQueue.pollFirst(waitTimePoolLinkCheckRequestQueue.getSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("The Link Check Request Queue is unresponsive, operation timed out, {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        return null;
    }

    private LinkCheckResult attendLinkCheckRequest(LinkCheckRequest linkCheckRequest) {
        LinkCheckerReport linkCheckerReport;
        try {
            logger.info("Attending link check request for URL '{}'", linkCheckRequest.getUrl());
            URL linkCheckRequestUrl = new URL(linkCheckRequest.getUrl());
            linkCheckerReport = linkCheckingStrategy.check(linkCheckRequestUrl, linkCheckRequest.shouldAccept401or403());
        } catch (LinkCheckerException | MalformedURLException e) {
            logger.warn("Could not attend link checking request for URL '{}'", linkCheckRequest.getUrl());
            logger.debug("Exception thrown", e);
            linkCheckerReport = new LinkCheckerReport();
            linkCheckerReport.setUrl(linkCheckRequest.getUrl());
            linkCheckerReport.setUrlAssessmentOk(false);
        }
        // Log the results
        logger.info("Link Check result for URL '{}', HTTP Status '{}', assessment '{}'",
                linkCheckerReport.getUrl(),
                linkCheckerReport.getHttpStatus(),
                linkCheckerReport.isUrlAssessmentOk() ? "OK" : "NOT OK");
        return LinkCheckModelsHelper.getResultFromReport(linkCheckerReport, linkCheckRequest);
    }

    private void attendLinkCheckResult(LinkCheckResult linkCheckResult) {
        if (linkCheckResult != null) {
            persist(linkCheckResult);
            announce(linkCheckResult);
        } else {
            logger.warn("Failed to attend link check");
        }
    }

    void persist(LinkCheckResult linkCheckResult) {
        try {
            linkCheckResultsService.save(linkCheckResult);
        } catch (LinkCheckResultServiceException e) {
            logger.error("COULD not save link check result for URL '{}', reason '{}'", linkCheckResult.getUrl(), e.getMessage());
        }
    }

    void announce(LinkCheckResult linkCheckResult) {
        try {
            linkCheckResultsPublisher.publish(linkCheckResult);
        } catch (PublisherException e) {
            logger.error("COULD not announce link check result for URL '{}', reason '{}'", linkCheckResult.getUrl(), e.getMessage());
        }
    }
}
