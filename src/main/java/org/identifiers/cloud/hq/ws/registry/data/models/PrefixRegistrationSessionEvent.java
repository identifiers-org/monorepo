package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2019-03-15 11:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the base class that represents the events that are part of a prefix registration request session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PrefixRegistrationSessionEvent {
    // TODO
    @Id
    @GeneratedValue
    private long id;

    
    private String eventName;
    private String actor;
    private String additionalInformation;
    private PrefixRegistrationSession prefixRegistrationSession;
    private Date created;
}
