package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSessionEventAccept;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2019-03-19 13:18
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface PrefixRegistrationSessionEventAcceptRepository extends JpaRepository<PrefixRegistrationSessionEventAccept, Long> {
    // TODO
    PrefixRegistrationSessionEventAccept findByPrefixRegistrationSessionId(long id);
}
