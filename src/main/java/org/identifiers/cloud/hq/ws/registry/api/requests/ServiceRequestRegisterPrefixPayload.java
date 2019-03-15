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

    // Unique identifier for this provider within the namespace for provider selection when resolving compact identifiers
    // belonging to the namespace being registered
    // TODO - PENDING VALIDATOR
    private String providerCode;

    // Originally 'homePage' on the identifiers.org form at https://identifiers.org/request/prefix, but now refactored
    // into a more sensible thing
    // TODO - THIS HAS BEEN REMOVED / REFACTORED INTO another attribute
    //private String homePage;

    // This is the name of the institution that owns the resource that's being registered as first provider for this namespace
    // TODO - PENDING VALIDATOR
    private String institutionName;

    // A description related to the institution that owns the resource that's being registered as first provider for this namespace
    // TODO - PENDING VALIDATOR
    private String institutionDescription;

    // Location of the institution that owns the resource that's being registered as first provider for this namespace
    // TODO - PENDING VALIDATOR
    private String institutionLocation;

    // This attribute is part of the original identifiers.org form at https://identifiers.org/request/prefix, but now
    // refactored into a more sensible thing
    // TODO - THIS HAS BEEN REMOVED / REFACTORED INTO another attribute
    //private String organization;

    // Originally called 'preferredPrefix', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    // TODO - PENDING VALIDATOR (refactor)
    private String requestedPrefix;

    // Originally called 'resourceAccessRule', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    // TODO - PENDING VALIDATOR (refactor)
    private String providerUrlPattern;

    
    private String exampleIdentifier;
    private String regexPattern;
    private String[] references;
    private String additionalInformation;
    private Requester requester;
}
