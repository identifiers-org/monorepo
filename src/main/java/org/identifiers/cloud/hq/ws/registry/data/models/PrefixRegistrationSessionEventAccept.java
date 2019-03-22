package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-19 11:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventAccept extends PrefixRegistrationSessionEvent {

    // Optional reason for accepting the prefix registration request
    private String acceptanceReason;

    public PrefixRegistrationSessionEventAccept() {
        super();
        this.setEventName("ACCEPT");
    }
}
