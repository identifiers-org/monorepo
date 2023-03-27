package org.identifiers.cloud.hq.ws.registry.models.validators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
public class WebPageCheckerDefault implements WebPageChecker {
    // By breaking different checking steps into externalized pieces, I can reuse this code on other possible new web
    // page checkers
    public boolean checkForValidUrl(String url) {
        // TODO - UrlValidator instance must be controlled by the running environment, as it should NOT accept localhost URLs in production
        return (new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS)).isValid(url);
    }

    // Necessary for test mocking
    public HttpURLConnection getConnection(URL testUrl) throws IOException {
        return (HttpURLConnection) testUrl.openConnection();
    }

    @SneakyThrows //to avoid malformed URL exception in redirection URL (url is checked before)
    public boolean checkForOkUrlEndpoint(String url) throws WebPageCheckerException {
        // Detect Dead endpoint by accessing it and making sure we get an HTTP 200 OK, in the future, more complex
        // checkers can be externalized, and pack as dead URL Endpoint detection strategies
        String newUrl = null;
        log.debug("Validating {}", url);
        HttpStatus status;
        try {
            // TODO - Add a re-try policy for when we try to connect the URL
            URL testUrl = new URL(url);
            HttpURLConnection connection = getConnection(testUrl);
            connection.setRequestMethod("GET");
            // TODO - Refactor this out as constants
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            status = HttpStatus.valueOf(connection.getResponseCode());
            if (status.is3xxRedirection()) {
                newUrl = connection.getHeaderField("Location");
            }
            connection.disconnect();
        } catch (IOException e) {
            log.error(String.format("Checking '%s' caused an error", url), e);
            throw new WebPageCheckerException(String.format("Checking '%s' caused an error: '%s'", url, e.getMessage()));
        }

        log.debug("Response code: {} / newUrl: {}", status, newUrl);

        // If redirection is a simple protocol change
        if (status.is3xxRedirection() && newUrl != null) {
            if (newUrl.toLowerCase().startsWith("https") &&
                    url.toLowerCase().startsWith("http") &&
                    newUrl.substring(5).equals(url.substring(4))) {
                throw new WebPageCheckerException("It seems that a https rewrite is in place. Use https instead of http.");
            } else {
                throw new WebPageCheckerException(
                        String.format("An URL rewrite is in place. The new URL is %s. You should rewrite your URL pattern.",
                                newUrl)
                );
            }
        }
        if (!status.is2xxSuccessful()) {
            throw new WebPageCheckerException(
                    String.format("'%s' returned status code %d, it must return 200", url, status.value()));
        }
        return true;
    }



    public boolean checkWebPageUrl(String webPageUrl) throws WebPageCheckerException {
        // Check for invalid URL
        if (!checkForValidUrl(webPageUrl)) {
            throw new WebPageCheckerException(String.format("'%s' is not a valid URL", webPageUrl));
        }
        return checkForOkUrlEndpoint(webPageUrl);
    }
}
