package org.identifiers.cloud.hq.ws.registry.models.validators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface WebPageChecker {
    boolean checkWebPageUrl(String webPageUrl) throws WebPageCheckerException;

    HttpURLConnection getConnection(URL testUrl) throws IOException;

    boolean checkForOkUrlEndpoint(String url) throws WebPageCheckerException;

    public boolean checkForValidUrl(String url);

}
