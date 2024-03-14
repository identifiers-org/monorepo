package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-10-10 14:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This document models an Institution than potentially owns resources (providers) in the registry.
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_name", columnList = "name", unique = true),
        @Index(name = "idx_institution_ror_id", columnList = "rorId", unique = true)})
public class Institution {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String homeUrl;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modified;

    @ManyToOne(optional = false)
    private Location location;

    // ROR IDs, long URL just in case...
    @Column(length = 2000, unique = true)
    private String rorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution that = (Institution) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(homeUrl, that.homeUrl) && Objects.equals(description, that.description) && Objects.equals(created, that.created) && Objects.equals(modified, that.modified) && Objects.equals(location, that.location) && Objects.equals(rorId, that.rorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, homeUrl, description, created, modified, location, rorId);
    }
}
