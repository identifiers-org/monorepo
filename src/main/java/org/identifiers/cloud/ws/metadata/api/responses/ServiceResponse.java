package org.identifiers.cloud.ws.metadata.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.identifiers.cloud.ws.metadata.api.ApiCentral;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.models.api.responses
 * Timestamp: 2018-03-06 11:32
 * ---
 */
@Getter @Setter @Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ServiceResponse<T extends Serializable> implements Serializable {
    private String apiVersion = ApiCentral.apiVersion;
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    private T payload;

    public static <T extends Serializable> ServiceResponse<T> of(T payload) {
        return new ServiceResponse<T>().setPayload(payload);
    }

    public static <T extends Serializable> ServiceResponse<T>
                ofError(HttpStatus status, String message) {
        return new ServiceResponse<T>()
                .setErrorMessage(message)
                .setHttpStatus(status);
    }
}
