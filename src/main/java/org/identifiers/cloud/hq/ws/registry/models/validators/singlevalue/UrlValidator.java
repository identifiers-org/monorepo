package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
@RequiredArgsConstructor
public class UrlValidator extends SingleValueValidator {
    final org.apache.commons.validator.routines.UrlValidator apacheUrlValidator =
            new org.apache.commons.validator.routines.UrlValidator(
                    org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS
            );
    final RestTemplate restTemplate;

    @Override
    public Optional<String> validate(String url, String valueLabel) {
        if (!checkForValidUrl(url)) {
            return Optional.of(valueLabel + " is not a valid URL");
        } else if (!startsWithIgnoreCase(url, "http")) {
            return Optional.of(valueLabel + " must use be a HTTP(S) URL");
        }

        log.debug("Validating HTTP response for {}", url);
        String newUrl = null;
        HttpStatusCode status;
        try {
            var response = restTemplate.exchange(url, GET, null, Void.class);
            status = response.getStatusCode();
            if (status.is3xxRedirection()) {
                newUrl = String.valueOf(response.getHeaders().getLocation());
            }
        } catch (RestClientException e) {
            log.error(String.format("Checking '%s' caused an error", url), e);
            return Optional.of("Checking " + url + " caused an error: '" + e.getMessage() + "'");
        }

        log.debug("Response code: {} / newUrl: {}", status, newUrl);

        return checkStatusCode(url, newUrl, status);
    }


    Optional<String> checkStatusCode(String url, String newUrl, HttpStatusCode status) {
        // If redirection is a simple protocol change
        if (status.is3xxRedirection() && newUrl != null && isHttpsRewrite(url, newUrl)) {
            return Optional.of("It seems that a https rewrite is in place. Use https instead of http.");
        }
        if (!status.is2xxSuccessful() && !status.is3xxRedirection()) {
            String message = String.format("'%s' returned invalid status code status code %d, " +
                    "should be a successful (2xx) or redirect (3xx) code", url, status.value());
            return Optional.of(message);
        }
        return Optional.empty();
    }


    boolean checkForValidUrl(String url) {
        return apacheUrlValidator.isValid(url);
    }


    boolean isHttpsRewrite(String url, String newUrl) {
        String newUrlWithoutProtocol = newUrl.substring(5);
        String urlWithoutProtocol = url.substring(4);
        return startsWithIgnoreCase(newUrl, "https") &&
               startsWithIgnoreCase(url, "http") &&
               newUrlWithoutProtocol.equals(urlWithoutProtocol);
    }
}
