package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-19 11:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventAccept extends PrefixRegistrationSessionEvent {

    // Optional reason for accepting the prefix registration request
    @Column(length = 2000)
    private String acceptanceReason;

    public PrefixRegistrationSessionEventAccept() {
        super();
        this.setEventName("ACCEPT");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrefixRegistrationSessionEventAccept that = (PrefixRegistrationSessionEventAccept) o;
        return Objects.equals(acceptanceReason, that.acceptanceReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), acceptanceReason);
    }
}
