package org.identifiers.cloud.ws.linkchecker.data.repositories;

import org.identifiers.cloud.ws.linkchecker.data.models.TrackedResource;
import org.springframework.data.repository.CrudRepository;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.repositories
 * Timestamp: 2018-06-12 13:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface TrackedResourceRepository extends CrudRepository<TrackedResource, String> {
    // TODO
}
