package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.MirIdService;
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
    @Autowired
    private NamespaceService namespaceService;
    @Autowired
    private MirIdService mirIdService;

    public Resource registerResource(Resource resource) throws ResourceServiceException {
        // TODO
        // TODO Resource instance validations via repository event handler
        // Current model definitions only allow for locating a resource via its MIR ID or the ID
        // It is not clear yet how to proceed as in: do we allow this method to be called with resources that have a
        // MIR ID or not? Let's go for 'yes, we allow this to be called with resources with an existing MIR ID, although
        // it may never happen, and we'll refine this in the future
        if (resource.getMirId() == null) {
            // TODO Request a MIR ID
            resource.setMirId(mirIdService.mintId());
        }
        // Register the contact person
        resource.setContactPerson(personService.registerPerson(resource.getContactPerson()));
        // TODO Register the location
        // TODO Register the institution
        // TODO Register the Namespace
        // TODO Register the resource
        // TODO Register the resource within its namespace
    }
}
