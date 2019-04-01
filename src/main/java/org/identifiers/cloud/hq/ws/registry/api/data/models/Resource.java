package org.identifiers.cloud.hq.ws.registry.api.data.models;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Resource implements Serializable {
    // identifiers.org internal ID for this resource
    private long id;
    private String mirId;
    private String urlPattern;
    // This is known as 'info' in the old data model from the EBI platform
    private String name;
    // Now you can optionally provide a description
    private String description;
    private boolean official;
    // We should require every provider to have a code
    private String providerCode;
    // This is a sample ID at provider level
    private String sampleId;
    // This is a home URL for this resource within the context of the namespace it belongs to
    private String resourceHomeUrl;
    private Institution institution;
    private Location location;

}
