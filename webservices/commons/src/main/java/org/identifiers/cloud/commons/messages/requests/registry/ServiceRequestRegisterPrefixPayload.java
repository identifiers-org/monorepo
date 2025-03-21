package org.identifiers.cloud.commons.messages.requests.registry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.models.Requester;

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
    // Name for the prefix being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String name;

    // This is a description for the namespace being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String description;

    // Home URL for a first provider of this namespace being registered
    private String providerHomeUrl;

    // Name for the provider being registered along this prefix
    private String providerName;

    // Description for the provider being registered along this prefix
    private String providerDescription;

    // Location Associated with the provider being registered along this prefix
    private String providerLocation;

    // Unique identifier for this provider within the namespace for provider selection when resolving compact identifiers
    // belonging to the namespace being registered
    private String providerCode;

    // This is the name of the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionName;

    // Home URL for the institution (this is a new requirement)
    private String institutionHomeUrl;

    // A description related to the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionDescription;

    // Location of the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionLocation;

    // ROR ID for this institution
    private String institutionRorId;

    // Originally called 'preferredPrefix', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String requestedPrefix;

    // Originally called 'resourceAccessRule', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String providerUrlPattern;

    // Originally called 'exampleIdentifier', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String sampleId;

    // Originally called 'regexPattern', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String idRegexPattern;

    private String[] supportingReferences;
    private String    additionalInformation;
    private Requester requester;

    // Flag whether this namespace has LUIs with namespace embedded, which is a special case, as they're allowed to omit their namespace when hitting the resolver.
    private boolean namespaceEmbeddedInLui = false;

    ////// Protected resource string
    private boolean protectedUrls;
    private boolean renderProtectedLanding;
    private String authHelpUrl;
    private String authHelpDescription;


}
