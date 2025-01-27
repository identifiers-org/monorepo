package org.identifiers.cloud.hq.ws.registry.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.responses
 * Timestamp: 2019-08-01 20:31
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServiceResponseDeactivateNamespacePayload implements Serializable {
    private String comment = "No comments on your namespace deactivation request";
}
