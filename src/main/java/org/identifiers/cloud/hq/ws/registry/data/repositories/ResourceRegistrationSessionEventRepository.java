package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSessionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2019-07-29 08:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ResourceRegistrationSessionEventRepository extends JpaRepository<ResourceRegistrationSessionEvent, Long> {
    // TODO
    List<ResourceRegistrationSessionEvent> findByResourceRegistrationSessionId(long id);
}
