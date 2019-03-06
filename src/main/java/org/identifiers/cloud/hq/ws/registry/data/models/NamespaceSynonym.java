package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 12:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Data model for namespace synonyms
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
// TODO - refactoring to relational
public class NamespaceSynonym {
    // This is the Synonym itself, obviously, it must be unique, otherwise we could not go from the synonym back to the
    // namespace
    @Id
    private String synonym;

    // The namespace this is a synonym of
    @ManyToOne(optional = false)
    private Namespace namespace;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

}
