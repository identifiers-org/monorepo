package org.identifiers.cloud.hq.ws.registry.data.repositories.curationwarnings;

import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurationWarningEventsRepository extends JpaRepository<CurationWarningEvent, Long> {
}
