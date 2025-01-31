package org.identifiers.cloud.commons.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.ApiCentral;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ServiceResponse<T> implements Serializable {
    private String apiVersion = ApiCentral.apiVersion;
    private String errorMessage = null;
    private T payload = null;

    @JsonIgnore
    private HttpStatusCode httpStatus = HttpStatus.OK;

    public ServiceResponse<T> setHttpStatus(HttpStatusCode httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ServiceResponse<T> setHttpStatus(int httpStatus) {
        this.httpStatus = HttpStatusCode.valueOf(httpStatus);
        return this;
    }

    public static <I> ServiceResponse<I> of(I payload) {
        return new ServiceResponse<I>()
                    .setPayload(payload);
    }

    public static <I> ServiceResponse<I> ofError(HttpStatus httpStatus, String errorMessage) {
        return new ServiceResponse<I>().setErrorMessage(errorMessage)
                                       .setHttpStatus(httpStatus);
    }
}