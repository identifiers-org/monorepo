package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-07-29 01:36
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
public class ResourceRegistrationSessionEventComment extends ResourceRegistrationSessionEvent {
    // This is the comment we want to add
    @Column(length = 2000)
    private String comment;

    public ResourceRegistrationSessionEventComment() {
        super();
        this.setEventName("COMMENT");
    }
}
