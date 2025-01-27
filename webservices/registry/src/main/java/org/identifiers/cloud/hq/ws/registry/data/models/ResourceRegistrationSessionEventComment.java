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
 * Timestamp: 2019-07-29 01:36
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResourceRegistrationSessionEventComment that = (ResourceRegistrationSessionEventComment) o;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), comment);
    }
}
