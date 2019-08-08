package org.identifiers.cloud.hq.ws.registry.data.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.MirIdService;
import org.identifiers.cloud.hq.ws.registry.models.MirIdServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    /**
     * Register a resource if not registered
     * @param resource resource to be registered
     * @return the registered resource
     * @throws ResourceServiceException
     */
    @Transactional
    public Resource registerResource(Resource resource) throws ResourceServiceException {
        // TODO Resource instance validations via repository event handler
        // Current model definitions only allow for locating a resource via its MIR ID or the ID
        // It is not clear yet how to proceed as in: do we allow this method to be called with resources that have a
        // MIR ID or not? Let's go for 'yes, we allow this to be called with resources with an existing MIR ID, although
        // it may never happen, and we'll refine this in the future
        if (resource.getMirId() == null) {
            // Request a MIR ID
            try {
                resource.setMirId(mirIdService.mintId());
                log.info(String.format("Registering resource with name '%s', for namespace '%s', newly minted MIR ID '%s'",
                        resource.getName(), resource.getNamespace().getPrefix(), resource.getMirId()));
            } catch (MirIdServiceException e) {
                throw new ResourceServiceException(String.format("Could not mint MIR ID for registering resource " +
                        "with name '%s', for namespace '%s', due to '%s'",
                        resource.getName(),
                        resource.getNamespace().getPrefix(),
                        e.getMessage()));
            }
        }
        // Register the contact person
        resource.setContactPerson(personService.registerPerson(resource.getContactPerson()));
        // Register the location
        resource.setLocation(locationService.registerLocation(resource.getLocation()));
        // Register the institution
        resource.setInstitution(institutionService.registerInstitution(resource.getInstitution()));
        // Register the Namespace
        resource.setNamespace(namespaceService.registerNamespace(resource.getNamespace()));
        // Register the resource within its namespace. Ok, why doing it this way? Because if this resource can't be
        // registered as provider within its associated namespace, the Namespace service will throw an exception that
        // will trigger a rollback in the database, as this method is transactional
        namespaceService.registerProvider(resource.getNamespace(), resource);
        // Register the resource
        Resource registeredResource = repository.save(resource);
        return registeredResource;
    }

    @Transactional
    public Resource appendResourceToNamespace(Resource resource, String namespacePrefix) throws ResourceServiceException {
        // TODO Resource instance validations via repository event handler
        // Current model definitions only allow for locating a resource via its MIR ID or the ID
        // It is not clear yet how to proceed as in: do we allow this method to be called with resources that have a
        // MIR ID or not? Let's go for 'yes, we allow this to be called with resources with an existing MIR ID, although
        // it may never happen, and we'll refine this in the future
        // TODO maybe think about future refactoring for the following code
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(String.format("RESOURCE REGISTRATION REQUEST - ACCEPT\n'%s'", objectMapper.writeValueAsString(resource)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (resource.getMirId() == null) {
            // Request a MIR ID
            try {
                resource.setMirId(mirIdService.mintId());
                log.info(String.format("Registering resource with name '%s', for namespace '%s', newly minted MIR ID '%s'",
                        resource.getName(), namespacePrefix, resource.getMirId()));
            } catch (MirIdServiceException e) {
                throw new ResourceServiceException(String.format("Could not mint MIR ID for registering resource " +
                                "with name '%s', for namespace '%s', due to '%s'",
                        resource.getName(),
                        namespacePrefix,
                        e.getMessage()));
            }
        }
        // Register the contact person
        log.info("Before contact person");
        resource.setContactPerson(personService.registerPerson(resource.getContactPerson()));
        log.info(String.format("For resource with name '%s', within namespace '%s', ASSOCIATED contact person with ID '%d'", resource.getName(), resource.getNamespace(), resource.getContactPerson().getId()));
        // Register the location
        resource.setLocation(locationService.registerLocation(resource.getLocation()));
        log.info(String.format("For resource with name '%s', within namespace '%s', ASSOCIATED location with ID '%s'", resource.getName(), resource.getNamespace(), resource.getLocation().getCountryCode()));
        // Register the institution
        resource.setInstitution(institutionService.registerInstitution(resource.getInstitution()));
        log.info(String.format("For resource with name '%s', within namespace '%s', ASSOCIATED institution with ID '%d'", resource.getName(), resource.getNamespace(), resource.getInstitution().getId()));
        // Retrieve the namespace by its prefix
        Namespace retrievedNamespace = namespaceService.getNamespaceByPrefix(namespacePrefix);
        if (retrievedNamespace == null) {
            throw new ResourceServiceException(String.format("Namespace '%s' NOT FOUND!!! When appending resource with name '%s'", namespacePrefix, resource.getName()));
        }
        resource.setNamespace(retrievedNamespace);
        log.info(String.format("For resource with name '%s', within namespace '%s', ASSOCIATED namespace with ID '%d'", resource.getName(), resource.getNamespace(), resource.getNamespace().getId()));
        // Register the resource within its namespace. Ok, why doing it this way? Because if this resource can't be
        // registered as provider within its associated namespace, the Namespace service will throw an exception that
        // will trigger a rollback in the database, as this method is transactional
        namespaceService.registerProvider(resource.getNamespace(), resource);
        // Register the resource
        Resource registeredResource = repository.save(resource);
        log.info(String.format("For resource with name '%s', within namespace '%s', REGISTERED with ID '%d'", resource.getName(), resource.getNamespace(), registeredResource.getId()));
        return registeredResource;

    }
}
