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
 * Timestamp: 2019-03-15 11:27
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a prefix registration session, it groups together the different stages in the process of
 * approving or rejecting a prefix registration request, and the evolution of its content.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_closed", columnList = "closed")})
public class PrefixRegistrationSession {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean closed = false;

    // This is a reference to the latest version of the prefix registration request that's being processed in the session.
    @OneToOne(optional = false)
    private PrefixRegistrationRequest prefixRegistrationRequest;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrefixRegistrationSession that = (PrefixRegistrationSession) o;
        return id == that.id && closed == that.closed && Objects.equals(prefixRegistrationRequest, that.prefixRegistrationRequest) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, closed, prefixRegistrationRequest, created);
    }
}
