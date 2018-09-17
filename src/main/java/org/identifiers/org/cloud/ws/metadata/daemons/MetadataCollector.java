package org.identifiers.org.cloud.ws.metadata.daemons;

import org.identifiers.org.cloud.ws.metadata.channels.PublisherException;
import org.identifiers.org.cloud.ws.metadata.channels.metadataExtractionResult.MetadataExtractionResultPublisher;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionRequest;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.identifiers.org.cloud.ws.metadata.data.models.MetadataExtractionResultFactory;
import org.identifiers.org.cloud.ws.metadata.data.services.MetadataExtractionResultService;
import org.identifiers.org.cloud.ws.metadata.data.services.MetadataExtractionResultServiceException;
import org.identifiers.org.cloud.ws.metadata.models.MetadataFetcher;
import org.identifiers.org.cloud.ws.metadata.models.MetadataFetcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    // Wire in a persistence service for metadata extraction results
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
        logger.info("Attending metadata extraction request for Access URL '{}', resolution path '{}'", request
                .getAccessUrl(), request.getResolutionPath());
        Object metadata = null;
        MetadataExtractionResult metadataExtractionResult = null;
        try {
            metadata = metadataFetcher.fetchMetadataFor(request.getAccessUrl());
            metadataExtractionResult = MetadataExtractionResultFactory.createResultWithMetadata()
                    .setHttpStatus(200)
                    .setMetadataContent(metadata);
        } catch (MetadataFetcherException e) {
            metadataExtractionResult.setErrorMessage(String.format("FAILED to fetch metadata for resolution path '%s'," +
                            " because '%s'",
                    request.getResolutionPath(),
                    e.getMessage()));
            // TODO I need to refine the error reporting here to correctly flag errors as client or server side
            if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.INTERNAL_ERROR.getValue()) {
                metadataExtractionResult.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.METADATA_NOT_FOUND.getValue()) {
                metadataExtractionResult.setHttpStatus(HttpStatus.NOT_FOUND.value());
            } else if (e.getErrorCode().getValue() == MetadataFetcherException.ErrorCode.METADATA_INVALID.getValue()) {
                metadataExtractionResult.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            } else {
                metadataExtractionResult.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            }

        }
        metadataExtractionResult
                .setAccessUrl(request.getAccessUrl())
                .setRequestTimestamp(request.getTimestamp().toString())
                .setResourceId(request.getResourceId())
                .setResolutionPath(request.getResolutionPath());
        return metadataExtractionResult;
    }

    private MetadataExtractionResult persist(MetadataExtractionResult result) {
        try {
            metadataExtractionResultService.save(result);
        } catch (MetadataExtractionResultServiceException e) {
            logger.error(String.format("FAILED to persist metadata extraction result due to '%s'", e.getMessage()));
        }
        return result;
    }

    private MetadataExtractionResult announce(MetadataExtractionResult result) {
        try {
            metadataExtractionResultPublisher.publish(result);
        } catch (PublisherException e) {
            logger.error("FAILED to announce metadata extraction result for Access URL '%s' due to '%s'", result
                    .getAccessUrl(), e.getMessage());
        }
        return result;
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
                // Pop element, if any, from the metadata extraction request queue
                logger.info("Polling metadata extraction request queue");
                MetadataExtractionRequest metadataExtractionRequest = nextMetadataExtractionRequest();
                if (metadataExtractionRequest == null) {
                    // If no element is in there, wait a random amount of time before trying again
                    logger.info("No Metadata Extraction request found");
                    randomWait();
                    continue;
                }
                // Process Metadata Extraction Request
                MetadataExtractionResult metadataExtractionResult = attendMetadataExtractionRequest(metadataExtractionRequest);
                if (metadataExtractionResult != null) {
                    persist(metadataExtractionResult);
                    announce(metadataExtractionResult);
                }
            } catch (RuntimeException e) {
                // Prevent the thread from crashing on any possible error
                logger.error("An error has been stopped for preventing the thread from crashing, '{}'", e.getMessage());
                randomWait();
            }
        }
        logger.info("--- [END] Metadata Collection Daemon ---");
    }

    @PostConstruct
    public void autoStartThread() {
        start();
    }


}
