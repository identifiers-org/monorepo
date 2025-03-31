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

    public static UrlAssessment ok() {
        return new UrlAssessment(HttpStatus.OK, null);
    }
    public static UrlAssessment ok(HttpStatusCode statusCode) {
        return new UrlAssessment(statusCode, null);
    }
    public static UrlAssessment notOk(HttpStatusCode statusCode, @NonNull String error) {
        assert StringUtils.hasText(error);
        return new UrlAssessment(statusCode, error);
    }

    public static UrlAssessment ok(int statusCode) {
        return ok(HttpStatus.valueOf(statusCode));
    }
    public static UrlAssessment notOk(int statusCode, @NonNull String error) {
        return notOk(HttpStatus.valueOf(statusCode), error);
    }
}
