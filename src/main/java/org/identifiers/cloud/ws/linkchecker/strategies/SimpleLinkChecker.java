package org.identifiers.cloud.ws.linkchecker.strategies;

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
public class SimpleLinkChecker implements LinkChecker {
    private static final int CONNECTION_TIMEOUT_SECONDS = 30;

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
        } catch (IOException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        // TODO
        return report;
    }
}
