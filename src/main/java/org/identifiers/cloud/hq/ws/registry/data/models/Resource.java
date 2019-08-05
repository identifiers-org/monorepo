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
 * Timestamp: 2018-10-11 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data model for a Resource (Provider) in the registry.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_mir_id", columnList = "mirId", unique = true),
        @Index(name = "idx_official", columnList = "official")})
public class Resource {
    @Id
    @GeneratedValue
    private long id;

    // TODO updates to this field should not be allowed via the REST repository (https://github.com/identifiers-org/cloud-hq-ws-registry/issues/45)
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
    @Column(nullable = false, columnDefinition = "boolean not null default false")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean deprecated = false;

    // Information on when this resource was deprecated
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date deprecationDate;

    @ManyToOne(optional = false)
    private Institution institution;

    @ManyToOne(optional = false)
    private Location location;

    @ManyToOne(optional = false)
    private Namespace namespace;

    @ManyToOne
    private Person contactPerson;
}
