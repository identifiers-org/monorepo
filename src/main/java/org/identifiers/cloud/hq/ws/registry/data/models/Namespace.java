package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_mir_id", columnList = "mirId", unique = true),
                    @Index(name = "idx_name", columnList = "name"),
                    @Index(name = "idx_prefix", columnList = "prefix", unique = true)})
public class Namespace {
    // Main internal ID for this namespace
    @Id
    @GeneratedValue
    private long id;

    // This is the prefix itself, i.e. what is actually used in the compact identifier.
    @Column(nullable = false, unique = true)
    private String prefix;

    // MIR ID associated with this namespace
    // TODO updates to this field should not be allowed via the REST repository (https://github.com/identifiers-org/cloud-hq-ws-registry/issues/45)
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date deprecationDate;

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
}
