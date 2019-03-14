package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;

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
    // By breaking different checking steps into externalized pieces, I can reuse this code on other possible new web
    // page checkers
    default boolean checkForValidUrl(String url) {
        // TODO - UrlValidator instance must be controlled by the running environment, as it should NOT accept localhost URLs in production
        return (new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS)).isValid(url);
    }

    default boolean checkForOkUrlEndpoint(String url) throws WebPageCheckerException {
        // Detect Dead endpoint by accessing it and making sure we get an HTTP 200 OK, in the future, more complex
        // checkers can be externalized, and pack as dead URL Endpoint detection strategies
        int status = 0;
        try {
            // TODO - Add a re-try policy for when we try to connect the URL
            URL testUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
            connection.setRequestMethod("GET");
            // TODO - Refactor this out as constants
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            status = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            // Including MalformedURLException
            throw new WebPageCheckerException(String.format("When checking your URL Endpoint, '%s', the following error occurred '%s'", url, e.getMessage()));
        }
        if (HttpStatus.valueOf(status) != HttpStatus.OK) {
            throw new WebPageCheckerException(String.format("When checking your URL Endpoint at '%s', we got the following HTTP Status Code %d", url, status));
        }
        return true;
    }

    default boolean checkWebPageUrl(String webPageUrl) throws WebPageCheckerException {
        // Check for invalid URL
        if (!checkForValidUrl(webPageUrl)) {
            throw new WebPageCheckerException(String.format("URL '%s' is NOT VALID", webPageUrl));
        }
        return checkForOkUrlEndpoint(webPageUrl);
    }
}
