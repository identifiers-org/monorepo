package org.identifiers.cloud.hq.ws.registry.api.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models how the microservice exposes information about resources in the registry through its Resolution API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Resource implements Serializable {
    private long id;
    private String mirId;
    private String accessUrl;
    private String info;
    private boolean official;
    private String providerCode;
    // TODO This should be Sample ID
    private String localId;
    // TODO This should be Resource Home URL
    private String resourceUrl;
    private String institution;
    private String location;
}
