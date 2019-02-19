package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:06
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ActiveMirIdRepository extends JpaRepository<ActiveMirId, Long> {

}
