package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ReturnedMirId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:30
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ReturnedMirIdRepository extends JpaRepository<ReturnedMirId, Long> {
    // NOTE - I'm not sure if I should hide this entire resource from the REST interface

    ReturnedMirId findByMirId(Long id);

    // This method will return the oldest deactivated MIR ID, i.e. the returned MIR ID that's been sitting there the
    // longest
    ReturnedMirId findTopByOrderByCreatedAsc();

    // I don't want removal operations available on the REST interface
    @RestResource(exported = false)
    @Transactional
    void deleteByMirId(long id);

    // I don't want new entries through REST API
    @RestResource(exported = false)
    ReturnedMirId save(ReturnedMirId returnedMirId);

    // Allow paginated listing of returned IDs
    Page<ReturnedMirId> findAll(Pageable pageable);

    // Do not allow general listing of returned IDs
    @RestResource(exported = false)
    List<ReturnedMirId> findAll();
}
