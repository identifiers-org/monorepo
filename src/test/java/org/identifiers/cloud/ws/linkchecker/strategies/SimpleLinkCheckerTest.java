package org.identifiers.cloud.ws.linkchecker.strategies;

import org.junit.runner.RunWith;
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

    public void reportOverviewTest() {
        LinkCheckerReport report = new SimpleLinkChecker().check("http://www.ebi.ac.uk/chebi/");
        // Just checking how the report looks like
        // TODO
    }
}