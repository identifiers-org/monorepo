package org.identifiers.cloud.ws.linkchecker.strategies;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    @Override
    public LinkCheckerReport check(String url) {
        LinkCheckerReport report = new LinkCheckerReport()
                .setUrl(url)
                .setTimestamp(new Timestamp(System.currentTimeMillis()));
        // TODO
        URL checkingUrl = null;
        try {
            checkingUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new SimpleLinkCheckerException(e.getMessage());
        }
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return report;
    }
}
