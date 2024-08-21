package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // TODO
    List<Resource> findAllByNamespaceId(long id);

    List<Resource> findAllByInstitutionId(long id);

    List<Resource> findByNamespaceIdAndProviderCode(long namespaceId, String providerCode);

    @RestResource(exported = false)
    List<Resource> findByProviderCode(String providerCode);
    Page<Resource> findByProviderCode(String providerCode, Pageable pageable);

    Resource findByMirId(String mirId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM resource " +
                    "WHERE SIMILARITY(url_pattern, ?1) > ?2 " +
                    "LIMIT 1")
    Resource findSimilarByUrlPattern(String providerUrlPattern, double similarityThreshold);
}
