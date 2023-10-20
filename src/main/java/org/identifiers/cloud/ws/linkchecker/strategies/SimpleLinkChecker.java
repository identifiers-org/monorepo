package org.identifiers.cloud.ws.linkchecker.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This class implements a simple link checking strategy based on HTTP GET request.
 */
@Component
@Scope("prototype")
public class SimpleLinkChecker implements LinkChecker {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLinkChecker.class);

    @Autowired
    @Qualifier("linkCheckerRestTemplate")
    RestTemplate restTemplate;

    @Override
    public LinkCheckerReport check(URL checkingUrl, boolean accept401or403) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(checkingUrl.toString())
                .setTimestamp(new Timestamp(System.currentTimeMillis()));

        ResponseEntity<Object> response = restTemplate.exchange(checkingUrl.toString(),
                HttpMethod.HEAD, null, Object.class);
        report.setHttpStatus(response.getStatusCodeValue());

        HttpStatus responseStatus = response.getStatusCode();
        if (responseStatus.is2xxSuccessful()) {
            report.setUrlAssessmentOk(true);
            logger.info("[HTTP {}] ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else if (responseStatus.is3xxRedirection()) {
            report.setUrlAssessmentOk(true);
            logger.info("[HTTP {}] REDIRECT ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else if (
                (responseStatus == HttpStatus.FORBIDDEN || responseStatus == HttpStatus.UNAUTHORIZED)
                        && accept401or403
        ) {
            report.setUrlAssessmentOk(true);
            logger.info("[HTTP {}] AUTH RESPONSE ACCEPTED AS OK For PROTECTED URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else {
            report.setUrlAssessmentOk(false);
            logger.info("[HTTP {}] NOT ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        }
        return report;
    }
}
