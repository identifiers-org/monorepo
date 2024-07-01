package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
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

    boolean existsNamespaceByPrefix(String prefix);

    // NOTE I just wanted to experiment with native queries, in order to optimize some operations
    @Query(value = "SELECT t.prefix FROM #{#entityName} t", nativeQuery = true)
    @RestResource(exported = false)
    List<Object[]> findAllPrefixes();

    @RestResource(exported = false)
    List<Namespace> findByPrefixStartsWith(String prefixStart);

    Page<Namespace> findByPrefixStartsWith(String prefixStart, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM #{#entityName} t WHERE t.prefix SIMILAR TO '[0-9]%'")
    Page<Namespace> findByPrefixStartsWithNumbers(Pageable pageable);

    @RestResource(exported = false)
    List<Namespace> findByPrefixContaining(String content);

    Page<Namespace> findByPrefixContaining(String content, Pageable pageable);

    Namespace findByMirId(String mirId);

    @Query("SELECT n FROM Namespace n WHERE n.modified > :date")
    @RestResource(exported = false)
    List<Namespace> findNamespacesModifiedSince(Date date);
}
