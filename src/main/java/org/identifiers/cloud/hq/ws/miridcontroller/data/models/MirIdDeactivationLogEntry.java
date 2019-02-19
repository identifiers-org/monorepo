package org.identifiers.cloud.hq.ws.miridcontroller.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.models
 * Timestamp: 2019-02-19 15:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents log entries with information on when a MIR ID is "returned", i.e. deactivated.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MirIdDeactivationLogEntry {
    // Internal ID for the entry
    @Id
    @GeneratedValue
    private long id;

    // MIR ID
    private long mirId;

    // When the log entry was created
    private Date created;

    // When the MIR ID was minted
    private Date minted;

    // When the MIR ID was last confirmed
    private Date lastConfirmed;

    // Some additional information regarding the deactivation entry
    private String additionalInformation;
    // TODO
}
