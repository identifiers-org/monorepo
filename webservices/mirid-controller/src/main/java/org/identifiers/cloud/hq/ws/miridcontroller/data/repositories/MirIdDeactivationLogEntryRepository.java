package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.MirIdDeactivationLogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface MirIdDeactivationLogEntryRepository extends JpaRepository<MirIdDeactivationLogEntry, Long> {
    // NOTE - Just restrict some operations on the REST API
    List<MirIdDeactivationLogEntry> findByMirId(long id);

    // Do not allow changes through the REST API
    @RestResource(exported = false)
    MirIdDeactivationLogEntry save(MirIdDeactivationLogEntry entry);
    @RestResource(exported = false)
    void deleteById(long id);

    // Allow segmented listing
    @RestResource(exported = false)
    List<MirIdDeactivationLogEntry> findAll();
    Page<MirIdDeactivationLogEntry> findAll(Pageable pageable);
//    List<MirIdDeactivationLogEntry> findAllByMirId(Pageable pageable);
}
