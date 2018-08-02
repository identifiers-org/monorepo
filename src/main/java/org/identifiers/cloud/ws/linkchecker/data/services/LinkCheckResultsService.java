package org.identifiers.cloud.ws.linkchecker.data.services;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.data.repositories.LinkCheckResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class LinkCheckResultsService {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultsService.class);

    @Autowired
    private LinkCheckResultRepository repository;

    public LinkCheckResult save(LinkCheckResult linkCheckResult) throws LinkCheckResultServiceException {
        try {
            repository.save(linkCheckResult);
        } catch (RuntimeException e) {
            throw new LinkCheckResultServiceException(e.getMessage());
        }
        return linkCheckResult;
    }
    // TODO
}
