package org.identifiers.cloud.hq.ws.registry.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-13 17:37
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a person's contact details (minimum)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_fullname", columnList = "fullName"),
                    @Index(name = "idx_email", columnList = "email", unique = true)})
public class Person {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modified;
}
