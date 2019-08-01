package org.identifiers.cloud.hq.ws.registry.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.requests
 * Timestamp: 2019-08-01 11:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestReactivateResourcePayload implements Serializable {
    // Right now, all we need to re-activate a resource, is its new URL pattern
    // Originally called 'resourceAccessRule', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String providerUrlPattern;
}
