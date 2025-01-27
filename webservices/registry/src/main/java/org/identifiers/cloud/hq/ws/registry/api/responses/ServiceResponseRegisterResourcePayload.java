package org.identifiers.cloud.hq.ws.registry.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.requests
 * Timestamp: 2019-07-25 13:18
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ServiceResponseRegisterResourcePayload implements Serializable {
    // Comments on the resource registration request
    private String comment = "No comments on your prefix registration request";
    // This is an ephemeral token whose lifecycle is tight to the lifecycle of the resource registration requests, so,
    // once the resource registration has been solved, either rejected or accepted, this token is no longer valid.
    private String token = "";
}
