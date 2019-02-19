package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ReturnedMirId;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:30
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ReturnedMirIdRepository extends JpaRepository<ReturnedMirId, Long> {
    // TODO
    ReturnedMirId findByMirId(Long id);

    ReturnedMirId findTopByOrderByCreatedAsc();

}
