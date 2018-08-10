package org.identifiers.cloud.ws.linkchecker.strategies;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
@Component
@Scope("prototype")
public class SimpleLinkChecker implements LinkChecker {
    private static final int CONNECTION_TIMEOUT_SECONDS = 3;
    private static final int READ_TIMEOUT_SECONDS = 7;

    @Override
    public LinkCheckerReport check(String url) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(url)
                .setTimestamp(new Timestamp(System.currentTimeMillis()));
        URL checkingUrl = null;
        try {
            checkingUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) checkingUrl.openConnection();
        } catch (IOException | ClassCastException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        connection.setConnectTimeout(CONNECTION_TIMEOUT_SECONDS * 1000);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(false);
        connection.setReadTimeout(READ_TIMEOUT_SECONDS);
        try {
            // TODO - This the operations that blocks
            report.setHttpStatus(connection.getResponseCode());
        } catch (IOException e) {
            throw new SimpleLinkCheckerException(String.format("IO Exception when checking '%s', reason '%s'", url, e.getMessage()));
        } finally {
            connection.disconnect();
        }
        if (report.getHttpStatus() == 200) {
            report.setUrlAssessmentOk(true);
        }
        // TODO - Log HTTP 3xx redirection destinations
        return report;
    }
}
