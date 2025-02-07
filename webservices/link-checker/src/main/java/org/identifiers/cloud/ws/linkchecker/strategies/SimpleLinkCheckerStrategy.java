package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;

import java.net.*;
import java.sql.Timestamp;

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
@Slf4j
public class SimpleLinkCheckerStrategy extends LinkCheckerStrategy {
    public SimpleLinkCheckerStrategy(String appVersion, String javaVersion,
                                     String appHomepage, UrlChecker urlChecker) {
        super(appVersion, javaVersion, appHomepage, urlChecker);
    }

    @Override
    @SneakyThrows(URISyntaxException.class)
    public LinkCheckerReport check(URL checkingUrl, boolean accept401or403) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(checkingUrl.toString())
                .setTimestamp(new Timestamp(System.currentTimeMillis()));

        var urlAssessment = urlChecker.check(checkingUrl, accept401or403);
        report.setUrlAssessmentOk(urlAssessment.isOk())
                .setHttpStatus(urlAssessment.statusCodeValue());

        return report;
    }
}
