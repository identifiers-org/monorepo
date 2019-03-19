package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2019-03-19 12:58
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface PrefixRegistrationRequestRepository extends JpaRepository<PrefixRegistrationRequest, Long> {
    // TODO
}
