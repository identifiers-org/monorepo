package org.identifiers.cloud.commons.urlchecking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
public class UrlChecker {
    static final Set<HttpStatusCode> STATUSES_TO_ACCEPT_WHEN_PROTECTED = Set.of(
            FORBIDDEN, UNAUTHORIZED, PROXY_AUTHENTICATION_REQUIRED, UNAVAILABLE_FOR_LEGAL_REASONS
    );

    private final HttpClient httpClient;

    public UrlAssessment check(String uriString, boolean isProtected) throws IllegalArgumentException {
        return check(URI.create(uriString), isProtected);
    }

    public UrlAssessment check(URL url, boolean isProtected) throws URISyntaxException {
        return check(url.toURI(), isProtected);
    }

    public static HttpRequest.Builder getBaseRequestBuilder(URI uri) {
        return HttpRequest.newBuilder().GET().uri(uri)
                .header("Accept", "*/*")
                .version(HttpClient.Version.HTTP_1_1);
    }

    public UrlAssessment check(URI uri, boolean isProtected) {
        var request = getBaseRequestBuilder(uri).build();
        return check(request, isProtected);
    }

    public UrlAssessment check(HttpRequest request, boolean isProtected) {
        var uri = request.uri().toString();
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            var code = response.statusCode();
            var httpStatus = HttpStatus.valueOf(code);

            if (httpStatus.is2xxSuccessful()) {
                log.info("[HTTP {}] ACCEPTED AS OK For URL {}", code, uri);
                return UrlAssessment.ok(httpStatus);
            } else if (httpStatus.is3xxRedirection()) {
                log.info("[HTTP {}] REDIRECT ACCEPTED AS OK For URL {}", code, uri);
                return UrlAssessment.ok(httpStatus);
            } else if (isProtected && STATUSES_TO_ACCEPT_WHEN_PROTECTED.contains(httpStatus)) {
                log.info("[HTTP {}] AUTH RESPONSE ACCEPTED AS OK For PROTECTED URL {}", code, uri);
                return UrlAssessment.ok(httpStatus);
            } else {
                log.info("[HTTP {}] NOT ACCEPTED AS OK For URL {}", code, uri);
                return UrlAssessment.notOk(httpStatus,
                        "Status " + httpStatus + " is not acceptable for URL " + uri);
            }
        } catch (ConnectException e) {
            log.warn("[HTTP NaN] Failed to connect to {}", uri);
            log.debug("message: {}", e.getMessage());
            return UrlAssessment.notOk(NOT_FOUND, "Failed to connect to server");
        } catch (SSLHandshakeException e) {
            log.warn("[HTTP NaN] SSL error on connect to {}", uri);
            log.debug("Stack trace:", e);
            return UrlAssessment.notOk(NOT_FOUND, "Failed to connect to server due to SSL error");
        } catch (IOException e) {
            log.warn("[HTTP NaN] IO Exception when connecting to {}", uri, e);
            return UrlAssessment.notOk(INTERNAL_SERVER_ERROR,
                    "Error trying to connect to server: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[HTTP NaN] Interrupted while checking URL {}", uri);
            return  UrlAssessment.notOk(INTERNAL_SERVER_ERROR, "Please try again");
        }
    }

}
