package org.identifiers.cloud.ws.linkchecker.data.services;

import org.identifiers.cloud.ws.linkchecker.data.repositories.LinkCheckResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.services
 * Timestamp: 2018-08-02 10:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This service offers high level business logic interface for persisting link check results.
 */
public class LinkCheckResultService {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultService.class);

    @Autowired
    private LinkCheckResultRepository linkCheckResultRepository;

    // TODO
}
