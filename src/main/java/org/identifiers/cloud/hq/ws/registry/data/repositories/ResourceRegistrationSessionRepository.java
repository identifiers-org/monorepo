package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2019-07-29 07:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ResourceRegistrationSessionRepository extends JpaRepository<ResourceRegistrationSession, Long> {
    // TODO
    Page<ResourceRegistrationSession> findByClosedFalse(Pageable pageable);
}
