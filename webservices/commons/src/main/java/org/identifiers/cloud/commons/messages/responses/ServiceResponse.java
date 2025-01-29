package org.identifiers.cloud.commons.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.ApiCentral;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ServiceResponse<T> implements Serializable {
    private String apiVersion = ApiCentral.apiVersion;
    private String errorMessage = null;
    private T payload = null;

    @JsonIgnore
    private HttpStatus httpStatus = HttpStatus.OK;

    public static <I> ServiceResponse<I> of(I payload) {
        return new ServiceResponse<I>()
                    .setPayload(payload);
    }
}