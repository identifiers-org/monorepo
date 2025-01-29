package org.identifiers.cloud.commons.messages.requests;

import lombok.Data;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.ApiCentral;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ServiceRequest<T> implements Serializable {
    private String apiVersion;
    private T payload;

    public static <I> ServiceRequest<I> of(I payload) {
        return new ServiceRequest<I>()
                    .setApiVersion(ApiCentral.apiVersion)
                    .setPayload(payload);
    }
}
