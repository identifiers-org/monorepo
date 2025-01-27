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
 * Timestamp: 2019-03-19 11:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventReject extends PrefixRegistrationSessionEvent {

    // Optional reason for rejecting the prefix registration request
    @Column(length = 2000)
    private String rejectionReason;

    public PrefixRegistrationSessionEventReject() {
        super();
        this.setEventName("REJECT");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrefixRegistrationSessionEventReject that = (PrefixRegistrationSessionEventReject) o;
        return Objects.equals(rejectionReason, that.rejectionReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rejectionReason);
    }
}
