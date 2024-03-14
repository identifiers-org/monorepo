package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:35
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ResourceRegistrationSessionEventAmend extends ResourceRegistrationSessionEvent {
    public ResourceRegistrationSessionEventAmend() {
        super();
        this.setEventName("AMEND");
    }
}
