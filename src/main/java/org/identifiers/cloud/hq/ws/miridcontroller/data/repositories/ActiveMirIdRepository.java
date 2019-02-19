package org.identifiers.cloud.hq.ws.miridcontroller.data.repositories;

import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.data.repositories
 * Timestamp: 2019-02-19 16:06
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ActiveMirIdRepository extends JpaRepository<ActiveMirId, Long> {

    // Exported methods on the REST API interface, although I may shut down some of them
    ActiveMirId findByMirId(BigInteger id);

}
