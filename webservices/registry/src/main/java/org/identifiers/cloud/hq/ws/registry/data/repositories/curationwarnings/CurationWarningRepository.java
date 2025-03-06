package org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings;

import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarning;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface CurationWarningRepository extends JpaRepository<CurationWarning, Long> {
    @RestResource(exported = false)
    Optional<CurationWarning> findByGlobalId(String globalId);

    @RestResource(path = "findByOpenStatus")
    Page<CurationWarning> findByOpen(boolean isOpen, Pageable pageable);

    @RestResource(path = "findBy")
    Page<CurationWarning> findAllBy(Example<CurationWarning> example, Pageable pageable);

    @RestResource(exported = false)
    @Query("select distinct cw.type from CurationWarning cw")
    List<String> findAllDistinctTypes();
}
