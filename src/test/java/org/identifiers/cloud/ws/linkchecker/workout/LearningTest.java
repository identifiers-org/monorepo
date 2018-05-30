package org.identifiers.cloud.ws.linkchecker.workout;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Deque;
import java.util.stream.IntStream;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.workout
 * Timestamp: 2018-05-30 12:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This unit test is for working out how things work with Spring Data Redis and backend operations.
 */
@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = ApplicationConfig.class)
@SpringBootTest
public class LearningTest {
    private static final Logger logger = LoggerFactory.getLogger(LearningTest.class);

    @Autowired
    private Deque<LinkCheckRequest> linkCheckRequestQueue;

    @Test
    public void queueLinkCheckRequestProvider() {
        IntStream.range(0, 50).parallel().forEach(i -> {
            logger.info("Queuing link checking request #{}", i);
            linkCheckRequestQueue.offerLast(new LinkCheckRequest()
                    .setProviderId(String.format("%d", i))
                    .setTimestamp(new Timestamp(System.currentTimeMillis()))
                    .setUrl("http://www.ebi.ac.uk/chebi/"));
        });
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore
        }*/
    }
}

