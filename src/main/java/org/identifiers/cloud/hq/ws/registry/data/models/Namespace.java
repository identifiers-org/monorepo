package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
// TODO - refactoring to relational
public class Namespace {
    // Main internal ID for this namespace
    @Id
    @GeneratedValue
    private long id;

    // This is the prefix itself, i.e. what is actually used in the compact identifier.
    @Column(nullable = false, unique = true)
    private String prefix;

    // MIR ID associated with this namespace
    @Column(nullable = false, unique = true)
    private String mirId;

    // Name for this namespace, this part does not have anything to do with the prefix in the compact identifier
    @Column(nullable = false)
    private String name;

    // This is the regular expression that describes the IDs within the ID space scoped by this namespace
    @Column(nullable = false)
    private String pattern;

    // Some literal description of the namespace
    @Column(nullable = false)
    private String description;

    // When the namespace was created (registered)
    private Date created;

    // This field holds information on the last time a namespace was modified
    private Date modified;

    // This field flags whether the namespace has been deprecated or not
    private boolean deprecated = false;

    // Information on when this namespace was deprecated
    private Date deprecationDate;

    // A namespace level sample ID
    private String sampleId;
}
