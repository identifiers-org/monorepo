package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.SneakyThrows;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

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
    public MultiUserAgentLinkCheckerStrategy(HttpClient linkCheckerHttpClient, String appVersion,
                                             String javaVersion, String appHomepage) {
        super(linkCheckerHttpClient, appVersion, javaVersion, appHomepage);
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
        for (String userAgent : this.userAgentsToUse) {
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(checkingUrl.toURI())
                    .header("Accept", "*/*")
                    .header("User-Agent", userAgent)
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            this.fillInReportForRequest(request, report, accept401or403);
            if (report.isUrlAssessmentOk()) {
                return report;
            }
        }
        return report;
    }
}
