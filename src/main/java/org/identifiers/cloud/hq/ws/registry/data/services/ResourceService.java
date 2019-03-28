package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2019-03-28 10:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Service implements complex persistence logic for resources.
 */
@Service
@Slf4j
public class ResourceService {
    // TODO
    // Repository
    @Autowired
    private ResourceRepository repository;

    // Services
    @Autowired
    private PersonService personService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private InstitutionService institutionService;
}
