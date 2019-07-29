package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:37
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
public class ResourceRegistrationSessionEventReject extends ResourceRegistrationSessionEvent {
    // Optional reason for rejecting the resource registration request
    @Column(length = 2000)
    private String rejectionReason;

    public ResourceRegistrationSessionEventReject() {
        super();
        this.setEventName("REJECT");
    }
}
