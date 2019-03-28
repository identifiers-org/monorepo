package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.data.repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2019-03-28 10:41
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This Service implements complex persistence logic for Institutions.
 */
@Service
@Slf4j
public class InstitutionService {
    // TODO
    // Repository
    @Autowired
    private InstitutionRepository repository;

    // Services
    @Autowired
    private LocationService locationService;

    public Institution registerInstitution(Institution institution) throws InstitutionServiceException {
        // TODO
        Institution registeredInstitution = repository.findByName(institution.getName());
        if (registeredInstitution == null) {
            // TODO
            // TODO - Run validations, probably through Repository Event listeners
            institution.setLocation(locationService.registerLocation(institution.getLocation()));
            registeredInstitution = repository.save(institution);
        }
        return registeredInstitution;
    }
}
