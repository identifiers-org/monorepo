package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
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
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {
    // TODO
    Namespace findByPrefix(String prefix);

    List<Namespace> findByPrefixStartsWith(String prefixStart);

    List<Namespace> findByPrefixContaining(String content);
}
