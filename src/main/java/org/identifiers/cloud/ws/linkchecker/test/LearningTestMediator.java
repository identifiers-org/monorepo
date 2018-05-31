package org.identifiers.cloud.ws.linkchecker.test;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Deque;
import java.util.stream.IntStream;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.test
 * Timestamp: 2018-05-31 10:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a mediator for LearningTest unit tests
 */
@Component
@Profile("test")
public class LearningTestMediator {
    private static final Logger logger = LoggerFactory.getLogger(LearningTestMediator.class);

    @Autowired
    private Deque<LinkCheckRequest> linkCheckRequestQueue;

    @PostConstruct
    public void postConstruct() {
        logger.info("LearningTestMediator instantiated");
    }

    public void testLinkCheckQueuing() {
        IntStream.range(0, 50).parallel().forEach(i -> {
            logger.info("Queuing link checking request #{}", i);
            linkCheckRequestQueue.offerLast(new LinkCheckRequest()
                    .setProviderId(String.format("%d", i))
                    //.setTimestamp(new Timestamp(System.currentTimeMillis()))
                    .setUrl("http://www.ebi.ac.uk/chebi/"));
        });
        while (!linkCheckRequestQueue.isEmpty()) {
            logger.info("Waiting for the link checking queue to be consumed");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Exit
                return;
            }
        }
    }

}
