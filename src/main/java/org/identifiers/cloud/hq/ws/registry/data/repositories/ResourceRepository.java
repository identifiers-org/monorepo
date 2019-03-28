package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2018-10-16 14:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // TODO
    List<Resource> findAllByNamespaceId(long id);

    List<Resource> findByProviderCode(String providerCode);
}
