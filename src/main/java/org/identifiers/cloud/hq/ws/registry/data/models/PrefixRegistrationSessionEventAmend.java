package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-19 11:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventAmend extends PrefixRegistrationSessionEvent {
    public PrefixRegistrationSessionEventAmend() {
        super();
        this.setEventName("AMEND");
    }
}
