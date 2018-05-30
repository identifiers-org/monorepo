package org.identifiers.cloud.ws.linkchecker.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 9:45
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkCheckerTest {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckerTest.class);

    @Autowired
    private LinkChecker linkChecker;

    @Autowired
    private Deque<LinkCheckRequest> linkCheckRequestQueue;

    @Test
    public void reportOverviewTest() {
        LinkCheckerReport report = linkChecker.check("http://www.ebi.ac.uk/chebi/");
        // Just checking how the report looks like
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Link Checker REPORT '{}'", mapper.writer().writeValueAsString(report));
        } catch (JsonProcessingException e) {
            logger.error("WTF! Error serializing a simple POJO!");
        }
    }

    @Test
    public void queueLinkCheckRequestProvider() {
        LinkCheckRequest linkCheckRequest = new LinkCheckRequest()
                .setProviderId("providerID1")
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .setUrl("http://www.ebi.ac.uk/chebi/");
        logger.info("Queuing link checking request for provider URL '{}'", linkCheckRequest.getUrl());
        linkCheckRequestQueue.offerLast(linkCheckRequest);
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
