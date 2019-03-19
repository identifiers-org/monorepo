package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-15 11:27
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a prefix registration session, it groups together the different stages in the process of
 * approving or rejecting a prefix registration requests, and the evolution of its content.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PrefixRegistrationSession {
    @Id
    @GeneratedValue
    private long id;

    // This is a reference to the latest version of the prefix registration request that's being processed in the session.
    @ManyToOne(optional = false)
    private PrefixRegistrationRequest prefixRegistrationRequest;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;
}
