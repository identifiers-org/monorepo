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
 * Timestamp: 2019-03-14 13:39
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
public class ServiceRequestRegisterPrefixPayload implements Serializable {
    // TODO - Refactor this API model to collect information for resource (provider) and institution as well.
    // Name for the prefix being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String name;

    // This is a description for the namespace being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String description;

    // Home URL for a first provider of this namespace being registered
    // TODO - PENDING VALIDATOR
    private String providerHomeUrl;

    // Name for the provider being registered along this prefix
    // TODO - PENDING VALIDATOR
    private String providerName;

    // Description for the provider being registered along this prefix
    // TODO - PENDING VALIDATOR
    private String providerDescription;

    // Location Associated with the provider being registered along this prefix
    // TODO - PENDING VALIDATOR
    private String providerLocation;

    
    private String providerCode;

    private String homePage;
    private String organization;
    private String preferredPrefix;
    private String resourceAccessRule;
    private String exampleIdentifier;
    private String regexPattern;
    private String[] references;
    private String additionalInformation;
    private Requester requester;
}
