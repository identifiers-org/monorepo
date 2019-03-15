package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-15 12:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a prefix registration request from the point of view of its persisted representation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PrefixRegistrationRequest {
    // TODO
    private long id;
    private String name;
    private String description;
    private String homePage;
    private String organization;
    private String prefix;
    private String urlPattern;
    private String sampleId;
    private String idRegexPattern;
    private String supportingReferences;
    private String additionalInformation;
    private String requesterName;
    private String requesterEmail;
}
