package org.identifiers.cloud.ws.linkchecker.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    @PostConstruct
    public void postConstruct() {
        logger.info("LearningTestMediator instantiated");
    }
}
