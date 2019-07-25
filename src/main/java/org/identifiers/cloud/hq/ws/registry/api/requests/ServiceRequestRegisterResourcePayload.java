package org.identifiers.cloud.hq.ws.registry.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.requests
 * Timestamp: 2019-07-25 12:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestRegisterResourcePayload implements Serializable {

    // Home URL for the resource
    private String providerHomeUrl;

    // Name for the provider being registered
    private String providerName;

    // Description for the provider being registered
    private String providerDescription;

    // Location Associated with the provider being registered
    private String providerLocation;

    // Unique identifier for this provider within the namespace for provider selection when resolving compact identifiers
    private String providerCode;

    // This is the name of the institution that owns the resource that's being registered
    private String institutionName;

    // Home URL for the institution (this is a new requirement)
    private String institutionHomeUrl;

    // A description related to the institution that owns the resource that's being registered
    private String institutionDescription;

    // Location of the institution that owns the resource that's being registered
    private String institutionLocation;

    // This is a sample LUI that is covered by the resource being registered
    private String sampleId;

    private String additionalInformation;
    private Requester requester;
}
