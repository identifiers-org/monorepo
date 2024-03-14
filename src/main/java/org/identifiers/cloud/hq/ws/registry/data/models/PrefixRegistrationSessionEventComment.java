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
 * Timestamp: 2019-03-19 11:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
public class PrefixRegistrationSessionEventComment extends PrefixRegistrationSessionEvent {
    // This is the comment we want to add
    @Column(length = 2000)
    private String comment;

    public PrefixRegistrationSessionEventComment() {
        super();
        this.setEventName("COMMENT");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrefixRegistrationSessionEventComment that = (PrefixRegistrationSessionEventComment) o;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), comment);
    }
}
