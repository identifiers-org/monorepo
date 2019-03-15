package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

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
    // TODO
    private long id;
    // Name, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String name;
    // A sensible description for this request, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String description;
    // Home page of this first resource / provider within the context of this new prefix, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String homePage;
    // Organization, ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String organization;
    // WARNING - This is missing in the original identifiers.org form at https://identifiers.org/request/prefix,
    // because I understand things where done by hand, but it is no longer the case
    private String organizationDescription;
    // WARNING - This is missing in the original identifiers.org form at https://identifiers.org/request/prefix,
    // because I understand things where done by hand, but it is no longer the case
    private String organizationLocation;
    // This is from 'preferredPrefix', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String prefix;
    // Originally called 'resourceAccessUrl', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String urlPattern;
    // Originally called 'exampleIdentifier', ported from the original identifiers.org form at https://identifiers.org/request/prefix
    private String sampleId;
    // Originally called regexPattern, ported from the original identifiers.org form at https://identifiers.org/request/prefix
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
