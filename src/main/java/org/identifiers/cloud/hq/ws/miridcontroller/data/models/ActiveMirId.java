package org.identifiers.cloud.hq.ws.miridcontroller.data.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.models
 * Timestamp: 2019-02-19 13:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents a MIR ID that is in use.
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
public class ActiveMirId {

    @Id
    @GeneratedValue
    private long id;

    
    private Date created;
    private Date lastConfirmed;
    // TODO
}
