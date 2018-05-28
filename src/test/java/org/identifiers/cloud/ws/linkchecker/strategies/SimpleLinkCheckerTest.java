package org.identifiers.cloud.ws.linkchecker.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 9:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleLinkCheckerTest {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLinkCheckerTest.class);

    public void reportOverviewTest() {
        LinkCheckerReport report = new SimpleLinkChecker().check("http://www.ebi.ac.uk/chebi/");
        // Just checking how the report looks like
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.debug(mapper.writer().writeValueAsString(report));
        } catch (JsonProcessingException e) {
            logger.error("WTF! Error serializing a simple POJO!");
        }
    }
}