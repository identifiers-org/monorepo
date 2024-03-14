package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:31
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
public class ResourceRegistrationSessionEventAccept extends ResourceRegistrationSessionEvent {
    // Optional reason for accepting the resource registration request
    @Column(length = 2000)
    private String acceptanceReason;

    public ResourceRegistrationSessionEventAccept() {
        super();
        this.setEventName("ACCEPT");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResourceRegistrationSessionEventAccept that = (ResourceRegistrationSessionEventAccept) o;
        return Objects.equals(acceptanceReason, that.acceptanceReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), acceptanceReason);
    }
}
