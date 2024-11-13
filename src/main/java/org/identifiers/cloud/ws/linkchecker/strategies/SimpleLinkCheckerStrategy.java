package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
    public SimpleLinkCheckerStrategy(HttpClient linkCheckerHttpClient, String appVersion,
                                     String javaVersion, String appHomepage) {
        super(linkCheckerHttpClient, appVersion, javaVersion, appHomepage);
    }

    @Override
    @SneakyThrows(URISyntaxException.class)
    public LinkCheckerReport check(URL checkingUrl, boolean accept401or403) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(checkingUrl.toString())
                .setTimestamp(new Timestamp(System.currentTimeMillis()));
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(checkingUrl.toURI())
                .header("Accept", "*/*")
                .header("User-Agent", this.idorgAgentStr)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        this.fillInReportForRequest(request, report, accept401or403);
        return report;
    }
}
