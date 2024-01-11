package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:06
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ActiveMirIdRepository extends JpaRepository<ActiveMirId, Long> {
    // NOTE - Should I take everything off the REST interface?
    // Exported methods on the REST API interface, although I may shut down some of them
    ActiveMirId findByMirId(long id);

    @Query("select coalesce(max(mirId), 0) from ActiveMirId")
    long getMaxMirId();

    // Hide this from the REST interface
    @RestResource(exported = false)
    @Transactional
    void deleteByMirId(long id);

    // Disable PUT and PATCH
    @RestResource(exported = false)
    ActiveMirId save(ActiveMirId activeMirId);

    // Do not allow unsegmented listing of active MIR IDs
    @RestResource(exported = false)
    List<ActiveMirId> findAll();

    // Allow listing of all Active MIR IDs, sorted
    List<ActiveMirId> findAll(Sort sort);

    // Allow paginated listing of all Active MIR IDs
    Page<ActiveMirId> findAll(Pageable pageable);
}
