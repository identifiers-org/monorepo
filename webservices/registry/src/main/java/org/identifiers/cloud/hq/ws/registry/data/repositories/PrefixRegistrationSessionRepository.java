package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2019-03-19 13:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface PrefixRegistrationSessionRepository extends JpaRepository<PrefixRegistrationSession, Long> {
    // TODO
    Page<PrefixRegistrationSession> findByClosedFalse(Pageable pageable);
}
