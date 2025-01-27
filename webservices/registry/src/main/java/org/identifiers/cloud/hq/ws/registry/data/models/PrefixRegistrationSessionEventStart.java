package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-19 11:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventStart extends PrefixRegistrationSessionEvent {
    public PrefixRegistrationSessionEventStart() {
        super();
        this.setEventName("START");
    }
}
