package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2018-10-16 14:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    // TODO
    Institution findByName(String name);

    @RestResource(exported = false)
    List<Institution> findByNameContaining(String nameContent);
    Page<Institution> findByNameContaining(String nameContent, Pageable pageable);

    @RestResource(exported = false)
    List<Institution> findByLocationCountryCode(String countryCode);
    Page<Institution> findByLocationCountryCode(String countryCode, Pageable pageable);
}
