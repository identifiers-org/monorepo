package org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings;

import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarning;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.InstitutionCurationWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RestResource(rel="curationWarnings")
public interface InstitutionCurationWarningRepository extends JpaRepository<InstitutionCurationWarning, Long> {
    @RestResource(path = "findByOpenStatus")
    Page<List<CurationWarning>> findByOpen(boolean isOpen, Pageable pageable);

    @RestResource(exported = false)
    @Query("select distinct cw.type from InstitutionCurationWarning cw")
    List<String> findAllDistinctTypes();
}
