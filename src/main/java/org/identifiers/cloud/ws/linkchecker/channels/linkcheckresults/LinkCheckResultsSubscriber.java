package org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults
 * Timestamp: 2018-08-02 14:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class LinkCheckResultsSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultsSubscriber.class);

    private Set<LinkCheckResultListener> listeners = new HashSet<>();
}
