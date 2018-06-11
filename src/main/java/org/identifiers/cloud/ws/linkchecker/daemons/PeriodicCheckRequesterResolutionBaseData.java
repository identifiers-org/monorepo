package org.identifiers.cloud.ws.linkchecker.daemons;

import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Deque;
import java.util.Random;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.daemons
 * Timestamp: 2018-05-31 15:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a check requester daemon that will use resolution insight data for periodically request link checking of
 * resources and providers.
 */
@Component
public class PeriodicCheckRequesterResolutionBaseData extends Thread {
    private static final int WAIT_TIME_MAX_BEFORE_NEXT_REQUEST_SECONDS = 86400;     // 24 hours
    private static final int WAIT_TIME_MIN_BEFORE_NEXT_REQUEST_SECONDS = 21600;     // 6 hours
    private static final int WAIT_TIME_ERROR_BEFORE_NEXT_REQUEST_SECONDS = 3600;    // 1 hour
    private static final Logger logger = LoggerFactory.getLogger(PeriodicCheckRequesterResolutionBaseData.class);

    private boolean shutdown = false;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.service.resolver.host}")
    private String wsResolverHost;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.service.resolver.port}")
    private String wsResolverPort;

    @Autowired
    private Deque<LinkCheckRequest> linkCheckRequestQueue;

    public synchronized boolean isShutdown() {
        return shutdown;
    }

    public synchronized void setShutdown() {
        this.shutdown = true;
    }

    @PostConstruct
    public void autoStartThread() {
        start();
    }

    @PreDestroy
    public void stopDaemon() {
        logger.info("--- [STOPPING] Periodic Link Check Requester on Resolution Base Data ---");
        setShutdown();
    }

    @Override
    public void run() {
        logger.info("--- [START] Periodic Link Check Requester on Resolution Base Data ---");
        Random random = new Random(System.currentTimeMillis());
        while (!isShutdown()) {
            // Next random number of seconds to wait before the next iteration
            int waitTimeSeconds = Math.min(WAIT_TIME_MIN_BEFORE_NEXT_REQUEST_SECONDS,
                    random.nextInt(WAIT_TIME_MAX_BEFORE_NEXT_REQUEST_SECONDS));
            // Get Resolution client and insight data on resolution samples, as they also contain the provider home URL,
            // we'll only need one request.
            ServiceResponseResolve insightResponse = ApiServicesFactory
                    .getResolverService(wsResolverHost, wsResolverPort)
                    .getAllSampleIdsResolved();
            if (insightResponse.getHttpStatus() == HttpStatus.OK) {
                // TODO
            } else {
                // TODO
                logger.error("Got HTTP Status '{}' from Resolution Service Insight API, reason '{}', " +
                        "SKIPPING this link checking request iteration",
                        insightResponse.getHttpStatus().value(),
                        insightResponse.getErrorMessage());
            }
            // TODO - Create link checking requests for resolution samples
            // TODO - Create link checking requests for home URLs (a.k.a. providers)
            // TODO - Wait for a random period of time before running another iteration
            // TODO
        }
    }
}
