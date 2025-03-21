package org.identifiers.cloud.commons.messages.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
    private String      resourceHomeUrl;
    private Institution institution;
    private Location    location;
    // This field flags whether the resource has been deprecated or not
    private boolean     deprecated;
    // Information on when this resource was deprecated
    private Date deprecationDate;
    private Date deprecationOfflineDate; // Approximation of when date was made unavailable
    private boolean renderDeprecatedLanding;
    private String deprecationStatement;

    private boolean protectedUrls;
    private boolean renderProtectedLanding;
    private String authHelpUrl;
    private String authHelpDescription;


}
