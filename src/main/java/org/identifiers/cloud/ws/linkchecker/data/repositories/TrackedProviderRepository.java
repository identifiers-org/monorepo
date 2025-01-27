package org.identifiers.cloud.ws.linkchecker.data.repositories;

import org.identifiers.cloud.ws.linkchecker.data.models.TrackedProvider;
import org.springframework.data.repository.CrudRepository;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.repositories
 * Timestamp: 2018-05-26 9:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface TrackedProviderRepository extends CrudRepository<TrackedProvider, String> {
    // TODO
}
