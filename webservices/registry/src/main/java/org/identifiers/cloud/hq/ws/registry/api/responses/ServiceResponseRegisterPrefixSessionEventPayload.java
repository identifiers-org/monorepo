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
 * Timestamp: 2019-03-29 11:30
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ServiceResponseRegisterPrefixSessionEventPayload implements Serializable {
    // Some information back to the client on the completion of the triggered event
    private String comment;
}
