package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Person;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PersonRepository;
import org.identifiers.cloud.hq.ws.registry.models.MirIdService;
import org.identifiers.cloud.hq.ws.registry.models.MirIdServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2019-03-25 13:37
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is service that implements complex persistence operations for Namespace objects
 */
@Service
@Slf4j
public class NamespaceService {

    // Repositories
    @Autowired
    private NamespaceRepository namespaceRepository;
    @Autowired
    private PersonRepository personRepository;
    // END - Repositories

    // Services
    @Autowired
    private MirIdService mirIdService;
    // END - Services

    public Namespace registerNewNamespace(Namespace namespace) throws NamespaceServiceException {
        // Check that you're not trying to register an already existing namespace
        if (namespaceRepository.findByPrefix(namespace.getPrefix()) != null) {
            throw new NamespaceServiceException(String.format("CANNOT register namespace '%s', " +
                    "because IT IS ALREADY REGISTERED", namespace.getPrefix()));
        }
        // Check if the person needs to be created or not
        Person contactPerson = personRepository.findByEmail(namespace.getContactPerson().getEmail());
        if (contactPerson == null) {
            log.info(String.format("REGISTERING NAMESPACE '%s', contact person with e-mail '%s', full name '%s'",
                    namespace.getPrefix(),
                    namespace.getContactPerson().getEmail(),
                    namespace.getContactPerson().getFullName()));
            // NOTE - I don't know JPA that well so that I can tell whether it does this automatically when persisting
            // a namespace or not
            namespace.setContactPerson(personRepository.save(namespace.getContactPerson()));
        } else {
            log.info(String.format("REGISTERING NAMESPACE '%s', with ALREADY EXISTING contact person with e-mail '%s', full name '%s'",
                    namespace.getPrefix(),
                    namespace.getContactPerson().getEmail(),
                    namespace.getContactPerson().getFullName()));
            namespace.setContactPerson(contactPerson);
        }
        // Get a MIR ID for the new namespace
        try {
            namespace.setMirId(mirIdService.mintId());
            log.info(String.format("REGISTERING NAMESPACE '%s', MIR ID minted '%s'",
                    namespace.getPrefix(),
                    namespace.getMirId()));
        } catch (MirIdServiceException e) {
            throw new NamespaceServiceException(String.format("REGISTERING NAMESPACE '%s', " +
                    "MIR ID minting resulted in the following error: '%s'",
                    namespace.getPrefix(),
                    e.getMessage()));
        }
        // Persist the new namespace
        Namespace registeredNamespace = namespaceRepository.save(namespace);
        log.info(String.format("REGISTERED NAMESPACE '%s', MIR ID '%s', internal ID '%d'",
                registeredNamespace.getPrefix(),
                registeredNamespace.getMirId(),
                registeredNamespace.getId()));
        return registeredNamespace;
    }
}
