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
 * Timestamp: 2019-03-15 11:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the base class that represents the events that are part of a prefix registration request session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_actor", columnList = "actor"),
                    @Index(name = "idx_event_name", columnList = "eventName")})
public class PrefixRegistrationSessionEvent {
    // TODO
    @Id
    @GeneratedValue
    private long id;

    // This part is useful for the client applications that will deal with session events
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String eventName;

    // This attribute should contain information on who triggered / created a particular event (it will usually be the
    // 'curator' that's working on the associated prefix registration session)
    @Column(nullable = false)
    private String actor;

    // Providing information associated with a particular event is useful, but optional
    private String additionalInformation;

    @ManyToOne(optional = false)
    private PrefixRegistrationSession prefixRegistrationSession;

    @ManyToOne
    private PrefixRegistrationRequest prefixRegistrationRequest;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;
}
