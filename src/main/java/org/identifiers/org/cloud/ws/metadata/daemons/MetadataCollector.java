package org.identifiers.org.cloud.ws.metadata.daemons;

import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionRequest;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.identifiers.org.cloud.ws.metadata.models.MetadataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.BlockingDeque;

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
    // TODO - Wire in a metadata extraction result publisher

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
    }

    private MetadataExtractionResult persist(MetadataExtractionResult result) {
        // TODO
    }

    private MetadataExtractionResult announce(MetadataExtractionResult result) {
        // TODO
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

}
