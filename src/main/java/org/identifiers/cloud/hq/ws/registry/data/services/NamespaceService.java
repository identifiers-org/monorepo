package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Person;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PersonRepository;
import org.identifiers.cloud.hq.ws.registry.models.MirIdService;
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

    // TODO
    public void registerNewNamespace(Namespace namespace) throws NamespaceServiceException {
        // TODO
        // Check that you're not trying to register an already existing namespace
        if (namespaceRepository.findByPrefix(namespace.getPrefix()) != null) {
            throw new NamespaceServiceException(String.format("CANNOT register namespace '%s', " +
                    "because IT IS ALREADY REGISTERED", namespace.getPrefix()));
        }
        // Check if the person needs to be created or not
        Person contactPerson = personRepository.findByEmail(namespace.getContactPerson().getEmail());
        if (contactPerson == null) {
            log.info("REGISTERING NAMESPACE '%s', contact person with e-mail '%s', full name '%s'",
                    namespace.getPrefix(),
                    namespace.getContactPerson().getEmail(),
                    namespace.getContactPerson().getFullName());
            // NOTE - I don't know JPA that well so that I can tell whether it does this automatically when persisting
            // a namespace or not
            namespace.setContactPerson(personRepository.save(namespace.getContactPerson()));
        } else {
            log.info("REGISTERING NAMESPACE '%s', with ALREADY EXISTING contact person with e-mail '%s', full name '%s'",
                    namespace.getPrefix(),
                    namespace.getContactPerson().getEmail(),
                    namespace.getContactPerson().getFullName());
            namespace.setContactPerson(contactPerson);
        }
        // TODO Get a MIR ID for the new namespace (libapi?)
        // TODO Persist the new namespace
    }
}
