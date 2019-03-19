package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-15 12:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a prefix registration request from the point of view of its persisted representation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PrefixRegistrationRequest {
    // TODO Define how these attributes are going to persist

    @Id
    @GeneratedValue
    private long id;

    // Name for the prefix being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    @Column(nullable = false, unique = true)
    private String name;

    // This is a description for the namespace being registered, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    @Column(nullable = false)
    private String description;

    // Home URL for a first provider of this namespace being registered
    @Column(nullable = false)
    private String providerHomeUrl;

    // Name for the provider being registered along this prefix
    @Column(nullable = false)
    private String providerName;

    // Description for the provider being registered along this prefix
    @Column(nullable = false)
    private String providerDescription;

    // Location Associated with the provider being registered along this prefix
    @Column(nullable = false)
    private String providerLocation;

    // Unique identifier for this provider within the namespace for provider selection when resolving compact identifiers
    // belonging to the namespace being registered
    private String providerCode;

    // This is the name of the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionName;

    // A description related to the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionDescription;

    // Location of the institution that owns the resource that's being registered as first provider for this namespace
    private String institutionLocation;

    // Originally called 'preferredPrefix', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String requestedPrefix;

    // Originally called 'resourceAccessRule', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String providerUrlPattern;

    // Originally called 'exampleIdentifier', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String sampleId;

    // Originally called 'regexPattern', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String idRegexPattern;

    // Originally called 'references', but, apparently, when hibernate produces the DDL, it doesn't do it very well and
    // the attribute name clashes with the reserved keyword 'references' of the SQL dialect used by the RDBMS backend,
    // ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String supportingReferences;

    // Additional information to be included as part of the request, ported from the original identifiers.org form at
    // https://identifiers.org/request/prefix
    private String additionalInformation;

    // Contact person for this request, ported from the original identifiers.org form at
    // https://identifiers.org/request/prefix
    private String requesterName;

    // Contact person for this request, ported from the original identifiers.org form at
    // https://identifiers.org/request/prefix
    private String requesterEmail;
}
