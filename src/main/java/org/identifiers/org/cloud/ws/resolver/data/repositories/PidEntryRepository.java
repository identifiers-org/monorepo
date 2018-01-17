package org.identifiers.org.cloud.ws.resolver.data.repositories;

import org.identifiers.org.cloud.ws.resolver.models.PidEntry;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.data.repositories
 * Timestamp: 2018-01-17 16:23
 * ---
 */
public interface PidEntryRepository extends CrudRepository<PidEntry, String> {

}
