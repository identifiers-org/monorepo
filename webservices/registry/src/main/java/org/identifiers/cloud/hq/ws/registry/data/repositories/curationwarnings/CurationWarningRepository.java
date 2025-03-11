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

    @RestResource(exported = false)
    List<CurationWarning> findByGlobalIdNotInAndOpenTrue(List<String> globalIds);

    @RestResource(exported = false)
    List<CurationWarning> findAllByOpenTrue();
}
