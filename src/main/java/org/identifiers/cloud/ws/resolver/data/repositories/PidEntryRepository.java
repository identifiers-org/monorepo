package org.identifiers.cloud.ws.resolver.data.repositories;

import org.identifiers.cloud.ws.resolver.data.models.PidEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.data.repositories
 * Timestamp: 2018-01-17 16:23
 * ---
 */
public interface PidEntryRepository extends CrudRepository<PidEntry, String> {

    // Find a PID Entry by prefix
    List<PidEntry> findByPrefix(String prefix);
}
