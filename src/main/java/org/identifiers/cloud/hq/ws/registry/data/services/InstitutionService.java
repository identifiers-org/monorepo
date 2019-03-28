package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
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


}
