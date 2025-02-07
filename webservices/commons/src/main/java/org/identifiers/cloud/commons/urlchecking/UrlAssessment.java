package org.identifiers.cloud.commons.urlchecking;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Value
public class UrlAssessment {
    HttpStatusCode statusCode;
    String error;

    private UrlAssessment(@NonNull HttpStatusCode statusCode, @Nullable String error) {
        this.statusCode = Objects.requireNonNull(statusCode);
        this.error = error;
    }

    public boolean isOk() {
        return !StringUtils.hasText(error);
    }

    public int statusCodeValue() {
        return statusCode.value();
    }

    static UrlAssessment ok(HttpStatusCode statusCode) {
        return new UrlAssessment(statusCode, null);
    }
    static UrlAssessment notOk(HttpStatusCode statusCode, String error) {
        return new UrlAssessment(statusCode, error);
    }

    static UrlAssessment ok(int statusCode) {
        return ok(HttpStatus.valueOf(statusCode));
    }
    static UrlAssessment notOk(int statusCode, String error) {
        return notOk(HttpStatus.valueOf(statusCode), error);
    }
}
