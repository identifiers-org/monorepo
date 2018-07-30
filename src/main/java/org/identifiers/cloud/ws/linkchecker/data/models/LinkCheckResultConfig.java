package org.identifiers.cloud.ws.linkchecker.data.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-07-30 11:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class is a delegate between the Spring based configuration and link check results, as they're not under control
 * of Spring Boot
 */
@Component
public class LinkCheckResultConfig {
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckResultConfig.class);

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.linkcheckresults.ttl.seconds}")
    private Long timeToLiveParam;


}
