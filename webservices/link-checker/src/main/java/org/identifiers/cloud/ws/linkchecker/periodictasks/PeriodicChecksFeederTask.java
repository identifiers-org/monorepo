package org.identifiers.cloud.ws.linkchecker.periodictasks;

import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

@Component
@ConditionalOnProperty(value = "org.identifiers.cloud.ws.linkchecker.daemon.periodicchecksfeedertask.enabled")
public class PeriodicChecksFeederTask implements Runnable{
    static final Logger logger = LoggerFactory.getLogger(PeriodicChecksFeederTask.class);
    static final Random random = new Random(System.currentTimeMillis());

    final BlockingDeque<LinkCheckRequest> linkCheckRequestQueue;
    final ResolverService resolverService;
    final Duration waitTimeMaxBeforeNextRequest;
    final Duration waitTimeMinBeforeNextRequest;
    final Duration waitTimeErrorBeforeNextRequest;

    public PeriodicChecksFeederTask(
            @Autowired BlockingDeque<LinkCheckRequest> linkCheckRequestQueue,
            @Autowired ResolverService resolverService,
            @Value("${org.identifiers.cloud.ws.linkchecker.daemon.periodicchecksfeedertask.waittime.error:1h}")
            Duration waitTimeErrorBeforeNextRequest,
            @Value("${org.identifiers.cloud.ws.linkchecker.daemon.periodicchecksfeedertask.waittime.min:12h}")
            Duration waitTimeMinBeforeNextRequest,
            @Value("${org.identifiers.cloud.ws.linkchecker.daemon.periodicchecksfeedertask.waittime.max:24h}")
            Duration waitTimeMaxBeforeNextRequest) {
        this.linkCheckRequestQueue = linkCheckRequestQueue;
        this.resolverService = resolverService;
        this.waitTimeMaxBeforeNextRequest = waitTimeMaxBeforeNextRequest;
        this.waitTimeMinBeforeNextRequest = waitTimeMinBeforeNextRequest;
        this.waitTimeErrorBeforeNextRequest = waitTimeErrorBeforeNextRequest;
    }

    long waitTimeSeconds;
    public long getNextWaitTimeSeconds() {
        return waitTimeSeconds;
    }

    @Override
    public void run() {
        logger.info("--- [START] Periodic Link Check Requester on Resolution Base Data ---");

        waitTimeSeconds = getRandomWaitTimeSeconds();

        // Get Resolution client and insight data on resolution samples, as they also contain the provider home URL,
        // we'll only need one request.
        ServiceResponse<ResponseResolvePayload> insightResponse = resolverService.getAllSampleIdsResolved();
        if (insightResponse.getHttpStatus().is2xxSuccessful()) {
            logger.info("Queuing link check requests for #{} entries from the Resolution insight API",
                    insightResponse.getPayload().getResolvedResources().size());
            insightResponse.getPayload().getResolvedResources()
                    .stream()
                    .filter(PeriodicChecksFeederTask::isNotDeprecated)
                    .forEach(resolvedResource -> {
                        linkCheckRequestQueue.add(new LinkCheckRequest()
                                .setUrl(resolvedResource.getCompactIdentifierResolvedUrl())
                                .setResourceId(Long.toString(resolvedResource.getId()))
                                .setAccept401or403(resolvedResource.isProtectedUrls()));
                        linkCheckRequestQueue.add(new LinkCheckRequest()
                                .setUrl(resolvedResource.getResourceHomeUrl())
                                .setProviderId(Long.toString(resolvedResource.getId()))
                                .setAccept401or403(false));
                    });
        } else {
            logger.error("Got HTTP Status '{}' from Resolution Service Insight API, reason '{}', " +
                            "SKIPPING this link checking request iteration",
                    insightResponse.getHttpStatus().value(),
                    insightResponse.getErrorMessage());
            // Adjust the time to wait before checking the insight api again
            waitTimeSeconds = getErrorWaitTimeSeconds();
        }
    }

    long getErrorWaitTimeSeconds() {
        return random.nextLong(waitTimeErrorBeforeNextRequest.getSeconds());
    }

    long getRandomWaitTimeSeconds() {
        return random.nextLong(waitTimeMinBeforeNextRequest.getSeconds());
    }

    private static boolean isNotDeprecated(ResolvedResource resource) {
        return !resource.isDeprecatedNamespace() && !resource.isDeprecatedResource();
    }
}
