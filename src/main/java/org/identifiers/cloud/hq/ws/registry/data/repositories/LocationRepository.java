package org.identifiers.cloud.hq.ws.registry.data.repositories;

import org.identifiers.cloud.hq.ws.registry.data.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.repositories
 * Timestamp: 2018-10-16 14:14
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface LocationRepository extends JpaRepository<Location, String> {
    // TODO
    Location findByCountryCode(String countryCode);
}
