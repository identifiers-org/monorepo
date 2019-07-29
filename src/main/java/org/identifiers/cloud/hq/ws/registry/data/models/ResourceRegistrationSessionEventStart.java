package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
public class ResourceRegistrationSessionEventStart extends ResourceRegistrationSessionEvent {
    public ResourceRegistrationSessionEventStart() {
        super();
        this.setEventName("START");
    }
}
