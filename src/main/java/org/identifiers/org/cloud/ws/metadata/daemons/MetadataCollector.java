package org.identifiers.org.cloud.ws.metadata.daemons;

import org.identifiers.org.cloud.ws.metadata.channels.metadataExtractionResult.MetadataExtractionResultPublisher;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionRequest;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.identifiers.org.cloud.ws.metadata.data.services.MetadataExtractionResultService;
import org.identifiers.org.cloud.ws.metadata.models.MetadataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.daemons
 * Timestamp: 2018-09-17 10:27
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class MetadataCollector extends Thread {
    private static final int WAIT_TIME_LIMIT_SECONDS = 30;
    private static final int WAIT_TIME_POLL_METADATA_EXTRACTION_REQUEST_QUEUE_SECONDS = 3;
    private static final Logger logger = LoggerFactory.getLogger(MetadataCollector.class);

    // Shutdown flag
    private boolean shutdown = false;
    // For random waits
    private Random random = new Random(System.currentTimeMillis());
    @Autowired
    private BlockingDeque<MetadataExtractionRequest> metadataExtractionRequestQueue;
    @Autowired
    private MetadataFetcher metadataFetcher;
    // Wire in a metadata extraction result publisher
    @Autowired
    private MetadataExtractionResultPublisher metadataExtractionResultPublisher;
    // TODO - Wire in a persistence service for metadata extraction results
    @Autowired
    private MetadataExtractionResultService metadataExtractionResultService;

    // Shutdown mechanism
    public synchronized boolean isShutdown() {
        return shutdown;
    }

    public synchronized void setShutdown() {
        logger.warn("--- [SHUTDOWN] REQUESTED ---");
        this.shutdown = true;
    }

    // Helpers
    private MetadataExtractionResult attendMetadataExtractionRequest(MetadataExtractionRequest request) {
        // TODO
        return null;
    }

    private MetadataExtractionResult persist(MetadataExtractionResult result) {
        // TODO
        return null;
    }

    private MetadataExtractionResult announce(MetadataExtractionResult result) {
        // TODO
        return null;
    }

    private void randomWait() {
        try {
            long waitTimeSeconds = random.nextInt(WAIT_TIME_LIMIT_SECONDS);
            logger.info("Random wait {}s", waitTimeSeconds);
            Thread.sleep(waitTimeSeconds * 1000);
        } catch (InterruptedException e) {
            logger.warn("The Metadata Collector Daemon has been interrupted while waiting for " +
                    "another iteration. Stopping the daemon, no more metadata extraction requests will be processed");
            setShutdown();
        }
    }

    private MetadataExtractionRequest nextMetadataExtractionRequest() {
        try {
            return metadataExtractionRequestQueue.pollFirst(WAIT_TIME_POLL_METADATA_EXTRACTION_REQUEST_QUEUE_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("The Metadata Extraction Request Queue is unresponsive, operation timed out, {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void run() {
        logger.info("--- [START] Metadata Collection Daemon ---");

        while (!isShutdown()) {
            try {
                // TODO
            } catch (RuntimeException e) {
                // Prevent the thread from crashing on any possible error
                logger.error("An error has been stopped for preventing the thread from crashing, '{}'", e.getMessage());
                randomWait();
            }
        }
        logger.info("--- [END] Metadata Collection Daemon ---");

    }
}
