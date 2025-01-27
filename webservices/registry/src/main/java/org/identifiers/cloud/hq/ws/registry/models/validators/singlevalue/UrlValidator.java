package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@RequiredArgsConstructor
public class UrlValidator extends SingleValueValidator {
    final org.apache.commons.validator.routines.UrlValidator apacheUrlValidator =
            new org.apache.commons.validator.routines.UrlValidator(
                    org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS
            );
    final RestTemplate restTemplate;

    static final Set<HttpStatusCode> STATUSES_TO_ACCEPT_WHEN_PROTECTED = Set.of(
            FORBIDDEN, UNAUTHORIZED, PROXY_AUTHENTICATION_REQUIRED, UNAVAILABLE_FOR_LEGAL_REASONS
    );



    @Override
    public Optional<String> validate(String url, String valueLabel) {
        return validate(url, valueLabel, false);
    }



    public Optional<String> validate(String url, String valueLabel, boolean isProtected) {
        if (!apacheUrlValidator.isValid(url)) {
            return Optional.of(valueLabel + " is not a valid URL, make sure it starts with 'http://' or 'https://'");
        } else if (!StringUtils.startsWithIgnoreCase(url, "http")) {
            return Optional.of(valueLabel + " must use be a HTTP(S) URL");
        }

        log.debug("Validating HTTP response for {}", url);
        String newUrl = null;
        HttpStatusCode status;
        try {
            var response = restTemplate.getForEntity(url, Void.class);
            status = response.getStatusCode();
            if (status.is3xxRedirection()) {
                newUrl = String.valueOf(response.getHeaders().getLocation());
            }
        } catch (RestClientException e) {
            log.error(String.format("Checking '%s' caused an error", url), e);
            if (e.getCause() instanceof UnknownHostException) {
                return Optional.of("Failed to locate host for " + url);
            } else {
                return Optional.of("Checking " + url + " caused an error: '" + e.getMessage() + "'");
            }
        }

        log.debug("Response code: {} / newUrl: {}", status, newUrl);

        return checkStatusCode(url, newUrl, status, isProtected);
    }



    Optional<String> checkStatusCode(String url, String newUrl, HttpStatusCode status, boolean isProtected) {
        // If redirection is a simple protocol change
        if (status.is3xxRedirection() && StringUtils.isNotBlank(newUrl) && isHttpsRewrite(url, newUrl)) {
            return Optional.of("It seems that a https rewrite is in place. Use https instead of http.");
        }
        if (STATUSES_TO_ACCEPT_WHEN_PROTECTED.contains(status) && isProtected) {
            return Optional.empty();
        }
        if (!status.is2xxSuccessful() && !status.is3xxRedirection()) {
            String message = String.format("'%s' resulted in invalid status code %s, " +
                    "only acceptable codes are successful (2xx) or redirect (3xx)", url, status);
            return Optional.of(message);
        }
        return Optional.empty();
    }



    boolean isHttpsRewrite(String url, String newUrl) {
        String newUrlWithoutProtocol = ensureNoTrailingSlash(newUrl.substring(5));
        String urlWithoutProtocol = ensureNoTrailingSlash(url.substring(4));
        return StringUtils.startsWithIgnoreCase(newUrl, "https") &&
                StringUtils.startsWithIgnoreCase(url, "http") &&
                StringUtils.equals(newUrlWithoutProtocol, urlWithoutProtocol);
    }

    private String ensureNoTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length()-1);
        } else {
            return url;
        }
    }
}
