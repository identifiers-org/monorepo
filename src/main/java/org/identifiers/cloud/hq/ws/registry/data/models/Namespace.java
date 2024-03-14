package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Past;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a Prefix (or namespace) in the registry
 */
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_mir_id", columnList = "mirId", unique = true),
                    @Index(name = "idx_name", columnList = "name"),
                    @Index(name = "idx_prefix", columnList = "prefix", unique = true)})
public class Namespace {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace namespace = (Namespace) o;
        return id == namespace.id && deprecated == namespace.deprecated && renderDeprecatedLanding == namespace.renderDeprecatedLanding && namespaceEmbeddedInLui == namespace.namespaceEmbeddedInLui && Objects.equals(prefix, namespace.prefix) && Objects.equals(mirId, namespace.mirId) && Objects.equals(name, namespace.name) && Objects.equals(pattern, namespace.pattern) && Objects.equals(description, namespace.description) && Objects.equals(created, namespace.created) && Objects.equals(modified, namespace.modified) && Objects.equals(deprecationDate, namespace.deprecationDate) && Objects.equals(deprecationOfflineDate, namespace.deprecationOfflineDate) && Objects.equals(deprecationStatement, namespace.deprecationStatement) && Objects.equals(infoOnPostmortemAccess, namespace.infoOnPostmortemAccess) && Objects.equals(successor, namespace.successor) && Objects.equals(sampleId, namespace.sampleId) && Objects.equals(contactPerson, namespace.contactPerson) && Objects.equals(resources, namespace.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix, mirId, name, pattern, description, created, modified, deprecated, deprecationDate, deprecationOfflineDate, renderDeprecatedLanding, deprecationStatement, infoOnPostmortemAccess, successor, sampleId, namespaceEmbeddedInLui, contactPerson, resources);
    }

    // Main internal ID for this namespace
    @Id
    @GeneratedValue
    private long id;

    // This is the prefix itself, i.e. what is actually used in the compact identifier.
    @Column(nullable = false, unique = true)
    private String prefix;

    // MIR ID associated with this namespace
    @Column(nullable = false, unique = true, updatable = false)
    private String mirId;

    // Name for this namespace, this part does not have anything to do with the prefix in the compact identifier
    @Column(nullable = false)
    private String name;

    // This is the regular expression that describes the IDs within the ID space scoped by this namespace
    @Column(nullable = false)
    private String pattern;

    // Some literal description of the namespace
    @Column(nullable = false, length = 2000)
    private String description;

    // When the namespace was created (registered)
    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    // This field holds information on the last time a namespace was modified
    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modified;

    // This field flags whether the namespace has been deprecated or not
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean deprecated = false;

    // Information on when this namespace was deprecated
    @Past
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date deprecationDate; // Date we marked as deprecated
    @Past
    private Date deprecationOfflineDate; // Approximation of when date was made unavailable

    private boolean renderDeprecatedLanding = false;
    private String deprecationStatement;
    private String infoOnPostmortemAccess;

    @OneToOne
    private Namespace successor; // Namespace that should be used in case of deprecation

    @AssertTrue(message = "Deprecation information is not allowed for not deprecated namespaces")
    public boolean deprecatedValuesOnlyIfDeprecated() {
        boolean hasAnyDeprecationValue = deprecationDate != null || deprecationOfflineDate != null ||
                                         deprecationStatement != null || infoOnPostmortemAccess != null ||
                                         successor != null;
        return deprecated || !hasAnyDeprecationValue;
    }


    // A namespace level sample ID
    @Column(nullable = false)
    private String sampleId;

    // This flag indicates whether LUIs in this namespace have embedded namespace prefix, e.g. GO ontology namespace
    // is 'go', and its LUIs are GO:00016912, usually, a compact identifier for this LUI would be go:GO:00016912, but
    // they are a special case allowed to be thrown at the resolver without stating the namespace, i.e. as GO:00016912.
    // This way, they can be reffered, at the resolver, as
    //      https://identifiers.org/GO:00016912
    //      https://identifiers.org/go/GO:00016912
    //      https://identifiers.org/providerCode/GO:00016912
    // Internally, their sample ID will be stored without the namespace information, because of the "purl" corner case,
    // i.e. purl URLs, like 'http://purl.obolibrary.org/obo/PR_000000024' where it is about ontology 'PR', whose LUIs
    // are like 'PR:000000024', but 'purl' changes the ':' by '_'. Thus, by storing only the part of the LUI after the
    // ':', and putting the 'PR' part followed by either '_' or ':', we can deal with this YASC (Yet Another Special
    // Case) of PIDs
    // P.S.: LUI (Locally Unique Identifiers), https://www.biorxiv.org/content/10.1101/101279v1.full
    // TODO This is possible to be modified here, but we need to remind the curator that provider pattern URLs should be updated as well.
    @Column(nullable = false)
    private boolean namespaceEmbeddedInLui = false;

    @ManyToOne
    private Person contactPerson;

    @OneToMany(mappedBy = "namespace")
    @ToString.Exclude
    private List<Resource> resources;
}
