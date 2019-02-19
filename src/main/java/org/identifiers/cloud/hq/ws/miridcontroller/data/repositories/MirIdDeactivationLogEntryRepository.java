package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.MirIdDeactivationLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface MirIdDeactivationLogEntryRepository extends JpaRepository<MirIdDeactivationLogEntry, Long> {
    // TODO
}
