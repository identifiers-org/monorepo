package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

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

    @RestResource(exported = false)
    List<Namespace> findByPrefixStartsWith(String prefixStart);

    Page<Namespace> findByPrefixStartsWith(String prefixStart, Pageable pageable);

    Page<Namespace> findByPrefixRegex(String prefixRegex, Pageable pageable);

    @RestResource(exported = false)
    List<Namespace> findByPrefixContaining(String content);

    Page<Namespace> findByPrefixContaining(String content, Pageable pageable);

}
