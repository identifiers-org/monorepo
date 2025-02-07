package org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;
import org.identifiers.cloud.hq.ws.registry.models.validators.SingleValueValidator;
import org.springframework.http.*;

import java.util.Optional;

import static org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS;


@Slf4j
@RequiredArgsConstructor
public class UrlValueValidator extends SingleValueValidator {
    final UrlValidator apacheUrlValidator =
            new UrlValidator(ALLOW_LOCAL_URLS);
    final UrlChecker urlChecker;

    static final HttpHeaders headers = new HttpHeaders();
    static {
        headers.setAccept(MediaType.parseMediaTypes("*/*"));
    }

    @Override
    public Optional<String> validate(String url, String valueLabel) {
        return validate(url, valueLabel, false);
    }



    public Optional<String> validate(String url, String valueLabel, boolean isProtected) {
        if (!apacheUrlValidator.isValid(url)) {
            return Optional.of(valueLabel + " is not a valid URL, make sure it starts with 'http://' or 'https://'");
        } else if (!StringUtils.startsWithIgnoreCase(url, "http")) {
            return Optional.of(valueLabel + " must be a HTTP(S) URL");
        }

        log.debug("Validating HTTP response for {}", url);
        var urlAssessment = urlChecker.check(url, isProtected);
        return Optional.ofNullable(urlAssessment.getError());
    }
}
