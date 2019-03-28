package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Location;
import org.identifiers.cloud.hq.ws.registry.data.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2019-03-28 10:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class implements complex persistence logic for Location.
 */
@Service
@Slf4j
public class LocationService {
    // Repository
    @Autowired
    private LocationRepository repository;

    /**
     * Register the given location if not registered
     * @param location the location to register
     * @return the registered location
     * @throws LocationServiceException
     */
    public Location registerLocation(Location location) throws LocationServiceException {
        // TODO Do check for ISO 3166/MA Alpha-2 Country Codes compliance, this will be achieved via Event Handlers for JPA Repositories, "before create"
        Location registeredLocation = repository.findByCountryCode(location.getCountryCode());
        if (registeredLocation == null) {
            log.info(String.format("Registering Location, country code '%s', country name '%s'", location.getCountryCode(), location.getCountryName()));
            registeredLocation = repository.save(location);
        }
        return registeredLocation;
    }
}
