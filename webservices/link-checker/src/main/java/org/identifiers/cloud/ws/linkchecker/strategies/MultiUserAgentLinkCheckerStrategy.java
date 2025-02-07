package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.SneakyThrows;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;

import java.sql.Timestamp;
import java.util.List;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:40
 *
 * @author Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>
 * ---
 * <p>
 * This class implements a link checking strategy that makes multiple HTTP GET requests with different user agents to avoid basic bot detection.
 */
public class MultiUserAgentLinkCheckerStrategy extends LinkCheckerStrategy {
    private final List<String> userAgentsToUse;
    public MultiUserAgentLinkCheckerStrategy(String appVersion, String javaVersion,
                                             String appHomepage, UrlChecker urlChecker) {
        super(appVersion, javaVersion, appHomepage, urlChecker);
        userAgentsToUse = List.of(
                this.idorgAgentStr,// Actual agent first in case it is valid
                "", // Try blank agent before any fake user agent
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:131.0) Gecko/20100101 Firefox/131.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36"
        );
    }

    @Override
    @SneakyThrows(URISyntaxException.class)
    public LinkCheckerReport check(URL checkingUrl, boolean accept401or403) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(checkingUrl.toString())
                .setTimestamp(new Timestamp(System.currentTimeMillis()));

        var uri = checkingUrl.toURI();
        for (String userAgent : this.userAgentsToUse) {
            var request = UrlChecker.getBaseRequestBuilder(uri)
                    .header("User-Agent", userAgent).build();

            var urlAssessment = urlChecker.check(request, accept401or403);
            if (urlAssessment.isOk()) {
                report.setHttpStatus(urlAssessment.statusCodeValue())
                        .setUrlAssessmentOk(true);

                // Stop on the first UA that is OK
                return report;
            }
        }
        return report;
    }
}
