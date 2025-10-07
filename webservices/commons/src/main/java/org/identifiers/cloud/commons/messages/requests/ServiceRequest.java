package org.identifiers.cloud.commons.messages.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.ApiCentral;

import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Accessors(chain = true)
@Schema(description = "Identifiers.org service request")
public class ServiceRequest<T> implements Serializable {
    @Schema(description = "API version", allowableValues = ApiCentral.apiVersion, requiredMode = REQUIRED)
    private String apiVersion;
    @Schema(requiredMode = REQUIRED)
    private T payload;

    public static <I> ServiceRequest<I> of(I payload) {
        return new ServiceRequest<I>()
                    .setApiVersion(ApiCentral.apiVersion)
                    .setPayload(payload);
    }
}
