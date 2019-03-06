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
        @Index(name = "idx_official", columnList = "official", unique = true)})
public class Resource {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String mirId;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private boolean official;

    // We should require every provider to have a code
    private String providerCode;

    // This is a sample ID at provider level
    private String sampleId;

    @Column(nullable = false)
    private String resourceUrl;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modified;

    @ManyToOne(optional = false)
    private Institution institution;

    @ManyToOne(optional = false)
    private Location location;

    @ManyToOne(optional = false)
    private Namespace namespace;
}
