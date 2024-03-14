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
 * Timestamp: 2019-07-29 01:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the base class that represents the events that are part of a resource registration request session.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_resource_registration_session_event_actor", columnList = "actor"),
        @Index(name = "idx_resource_registration_session_event_event_name", columnList = "eventName")})
public class ResourceRegistrationSessionEvent {
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
    private ResourceRegistrationSession resourceRegistrationSession;

    @ManyToOne
    private ResourceRegistrationRequest resourceRegistrationRequest;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRegistrationSessionEvent that = (ResourceRegistrationSessionEvent) o;
        return id == that.id && Objects.equals(eventName, that.eventName) && Objects.equals(actor, that.actor) && Objects.equals(additionalInformation, that.additionalInformation) && Objects.equals(resourceRegistrationSession, that.resourceRegistrationSession) && Objects.equals(resourceRegistrationRequest, that.resourceRegistrationRequest) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, actor, additionalInformation, resourceRegistrationSession, resourceRegistrationRequest, created);
    }
}
