package org.identifiers.cloud.hq.ws.registry.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.responses
 * Timestamp: 2019-03-14 13:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ServiceResponseRegisterPrefixPayload implements Serializable {
    // Comments on the prefix registration request
    private String comment = "No comments on your prefix registration request";
    // This is an ephemeral token whose lifecycle is tight to the lifecycle of the prefix registration requests, so,
    // once the prefix registration has been solved, either rejected or accepted, this token is no longer valid.
    private String token = "";
}
