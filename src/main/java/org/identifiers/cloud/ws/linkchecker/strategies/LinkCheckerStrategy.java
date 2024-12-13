package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the interface of Link Checking strategies
 */
@Slf4j
public abstract class LinkCheckerStrategy {
    protected final String idorgAgentStr;
    protected final HttpClient linkCheckerHttpClient;
    protected LinkCheckerStrategy(HttpClient linkCheckerHttpClient, String appVersion,
                                  String javaVersion, String appHomepage) {
        this.linkCheckerHttpClient = linkCheckerHttpClient;
        this.idorgAgentStr = String.format("Java-http-client/%s +%s LinkChecker/%s", javaVersion, appHomepage, appVersion);
    }

    public abstract LinkCheckerReport check(URL url, boolean accept401or403) throws LinkCheckerException;

    /**
     * @param request to be performed
     * @param report report to be filled with outcome from request
     * @param accept401or403 whether to accept 401 and 403 as OK
     * @return whether an exception was thrown while performing the request
     */
    protected boolean performRequestAndFillReport(HttpRequest request,
                                                  LinkCheckerReport report,
                                                  boolean accept401or403) {
        HttpResponse<?> response = null;
        try {
            response = linkCheckerHttpClient.send(request, HttpResponse.BodyHandlers.discarding());
            report.setHttpStatus(response.statusCode());
        } catch (ConnectException e) {
            report.setHttpStatus(HttpStatus.NOT_FOUND.value());
            report.setUrlAssessmentOk(false);
            log.info("[HTTP NaN] Failed to open connection to {}", report.getUrl());
            return true;
        } catch (IOException | InterruptedException e) {
            report.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            report.setUrlAssessmentOk(false);
            if (log.isDebugEnabled()) {
                log.info("[HTTP NaN] Exception when checking {}", report.getUrl(), e);
            } else {
                log.info("[HTTP NaN] Exception when checking {}: {}", report.getUrl(), e.getMessage());
            }
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            return true;
        }

        report.setHttpStatus(response.statusCode());
        HttpStatus responseStatus = HttpStatus.valueOf(response.statusCode());
        if (responseStatus.is2xxSuccessful()) {
            report.setUrlAssessmentOk(true);
            log.info("[HTTP {}] ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else if (responseStatus.is3xxRedirection()) {
            report.setUrlAssessmentOk(true);
            log.info("[HTTP {}] REDIRECT ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else if (
                (responseStatus == HttpStatus.FORBIDDEN || responseStatus == HttpStatus.UNAUTHORIZED)
                        && accept401or403
        ) {
            report.setUrlAssessmentOk(true);
            log.info("[HTTP {}] AUTH RESPONSE ACCEPTED AS OK For PROTECTED URL {}",
                    report.getHttpStatus(), report.getUrl());
        } else {
            report.setUrlAssessmentOk(false);
            log.info("[HTTP {}] NOT ACCEPTED AS OK For URL {}",
                    report.getHttpStatus(), report.getUrl());
        }
        return false;
    }
}
