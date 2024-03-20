package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Past;
import java.util.Date;
import java.util.Objects;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data model for a Resource (Provider) in the registry.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Accessors(chain = true)
@Entity @EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_mir_id", columnList = "mirId", unique = true),
        @Index(name = "idx_official", columnList = "official")})
public class Resource {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String mirId;

    // URL with the '{$id}' placeholder
    @Column(nullable = false, length = 2000)
    private String urlPattern;

    // This is known as 'info' in the old data model from the EBI platform
    @Column(nullable = false)
    private String name;

    // Now you can optionally provide a description
    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private boolean official;

    // We should require every provider to have a code
    private String providerCode;

    // This is a sample ID at provider level
    private String sampleId;

    // This is a home URL for this resource within the context of the namespace it belongs to
    @Column(nullable = false, length = 2000)
    private String resourceHomeUrl;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modified;

    // This field flags whether the resource has been deprecated or not
    // TODO this definition should be updated for namespaces as well...
    @Column(nullable = false, columnDefinition = "boolean not null default false")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean deprecated = false;

    // Information on when this resource was deprecated
    @Past
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date deprecationDate;
    @Past
    private Date deprecationOfflineDate; // Approximation of when date was made unavailable

    @Column(nullable = false)
    private boolean renderDeprecatedLanding = false;
    private String deprecationStatement;

    @AssertTrue(message = "Deprecation information is not allowed for not deprecated resources")
    public boolean deprecatedValuesOnlyIfDeprecated() {
        boolean hasAnyDeprecationValue = deprecationDate != null ||
                                         deprecationOfflineDate != null ||
                                         deprecationStatement != null;
        return !deprecated || !hasAnyDeprecationValue;
    }

    @ManyToOne(optional = false)
    private Institution institution;

    @ManyToOne(optional = false)
    private Location location;

    @ManyToOne(optional = false)
    private Namespace namespace;

    @JsonIgnore
    @Column(name = "namespace_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long namespaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person contactPerson;

    @Column(nullable = false)
    private boolean protectedUrls = false;

    @Column(nullable = false)
    private boolean renderProtectedLanding = false;

    @URL(regexp = "^(http|https).*$")
    private String authHelpUrl;

    @Column(length = 2000)
    @Length(min = 50)
    private String authHelpDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return id == resource.id && official == resource.official && deprecated == resource.deprecated && renderDeprecatedLanding == resource.renderDeprecatedLanding && protectedUrls == resource.protectedUrls && renderProtectedLanding == resource.renderProtectedLanding && Objects.equals(mirId, resource.mirId) && Objects.equals(urlPattern, resource.urlPattern) && Objects.equals(name, resource.name) && Objects.equals(description, resource.description) && Objects.equals(providerCode, resource.providerCode) && Objects.equals(sampleId, resource.sampleId) && Objects.equals(resourceHomeUrl, resource.resourceHomeUrl) && Objects.equals(created, resource.created) && Objects.equals(modified, resource.modified) && Objects.equals(deprecationDate, resource.deprecationDate) && Objects.equals(deprecationOfflineDate, resource.deprecationOfflineDate) && Objects.equals(deprecationStatement, resource.deprecationStatement) && Objects.equals(institution, resource.institution) && Objects.equals(location, resource.location) && Objects.equals(namespace, resource.namespace) && Objects.equals(contactPerson, resource.contactPerson) && Objects.equals(authHelpUrl, resource.authHelpUrl) && Objects.equals(authHelpDescription, resource.authHelpDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mirId, urlPattern, name, description, official, providerCode, sampleId, resourceHomeUrl, created, modified, deprecated, deprecationDate, deprecationOfflineDate, renderDeprecatedLanding, deprecationStatement, institution, location, namespace, contactPerson, protectedUrls, renderProtectedLanding, authHelpUrl, authHelpDescription);
    }
}
