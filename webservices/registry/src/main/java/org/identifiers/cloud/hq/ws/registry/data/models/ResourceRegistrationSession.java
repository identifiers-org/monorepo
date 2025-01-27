package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a resource registration session, it groups together the different stages in the process of
 * approving or rejecting a resource registration request, and the evolution of its content.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_resource_registration_session_closed", columnList = "closed")})
public class ResourceRegistrationSession {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean closed = false;

    // This is a reference to the latest version of the resource registration request that's being processed in the session.
    @OneToOne(optional = false)
    private ResourceRegistrationRequest resourceRegistrationRequest;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRegistrationSession that = (ResourceRegistrationSession) o;
        return id == that.id && closed == that.closed && Objects.equals(resourceRegistrationRequest, that.resourceRegistrationRequest) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, closed, resourceRegistrationRequest, created);
    }
}
