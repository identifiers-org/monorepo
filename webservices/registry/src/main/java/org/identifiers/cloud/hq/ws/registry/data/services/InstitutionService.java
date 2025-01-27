package org.identifiers.cloud.hq.ws.registry.data.services;

import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.data.repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

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
    // Repository
    @Autowired
    private InstitutionRepository repository;

    // Services
    @Autowired
    private LocationService locationService;

    /**
     * Register an institution if not registered.
     * @param institution the institution to register
     * @return the registered institution
     * @throws InstitutionServiceException
     */
    @Transactional
    public Institution registerInstitution(Institution institution) throws InstitutionServiceException {
        Institution registeredInstitution = null;

        // If rorid given, find existing institution by rorid
        if (StringUtils.isNotBlank(institution.getRorId())) {
            registeredInstitution = repository.findByRorId(institution.getRorId());
        }

        if (registeredInstitution == null) { // If not found, try to find by name;
            registeredInstitution = repository.findByName(institution.getName().trim());
        }

        if (registeredInstitution == null) { // if still not found, just create a new one.
            institution.setLocation(locationService.registerLocation(institution.getLocation()));
            registeredInstitution = repository.save(institution);
            log.info(String.format("Registered Institution with ID '%d', name '%s'",
                    registeredInstitution.getId(), registeredInstitution.getName()));
        } else {
            log.info(String.format("Associating to existing institution with ID '%d', name '%s'",
                    registeredInstitution.getId(), registeredInstitution.getName()));
        }

        return registeredInstitution;
    }
}
