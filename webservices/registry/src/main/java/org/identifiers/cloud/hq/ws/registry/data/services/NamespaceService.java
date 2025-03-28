package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.MirIdService;
import org.identifiers.cloud.hq.ws.registry.models.MirIdServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;

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

    // Repository
    @Autowired
    private NamespaceRepository repository;
    @Autowired
    private ResourceRepository resourceRepository;

    // Services
    @Autowired
    private PersonService personService;
    @Autowired
    private MirIdService mirIdService;
    // END - Services

    /**
     * This method registers the given namespace, taking care on whether its contact person needs to be registered as
     * well or it already exists, and also the MIR ID for the about to be registered namespace.
     * @param namespace to be registered
     * @return the registered namespace
     * @throws NamespaceServiceException
     */
    @Transactional
    public Namespace registerNamespace(Namespace namespace) throws NamespaceServiceException {
        // Check that you're not trying to register an already existing namespace
        if (repository.findByPrefix(namespace.getPrefix()) != null) {
            throw new NamespaceServiceException(String.format("CANNOT register namespace '%s', " +
                    "because IT IS ALREADY REGISTERED", namespace.getPrefix()));
        }
        // Delegate on Person Service
        namespace.setContactPerson(personService.registerPerson(namespace.getContactPerson()));
        // Get a MIR ID for the new namespace
        try {
            namespace.setMirId(mirIdService.mintId());
            log.info("REGISTERING NAMESPACE '{}', MIR ID minted '{}'", namespace.getPrefix(), namespace.getMirId());
        } catch (MirIdServiceException e) {
            throw new NamespaceServiceException(String.format("REGISTERING NAMESPACE '%s', " +
                    "MIR ID minting resulted in the following error: '%s'",
                    namespace.getPrefix(),
                    e.getMessage()));
        }
        // Persist the new namespace
        Namespace registeredNamespace = repository.save(namespace);
        log.info("REGISTERED NAMESPACE '{}', MIR ID '{}', internal ID '{}'", registeredNamespace.getPrefix(), registeredNamespace.getMirId(), registeredNamespace.getId());
        return registeredNamespace;
    }

    /**
     * Given a prefix, get the namespace details
     * @param namespacePrefix the given prefix
     * @return namespace details object
     * @throws NamespaceServiceException
     */
    public Namespace getNamespaceByPrefix(String namespacePrefix) throws NamespaceServiceException {
        return repository.findByPrefix(namespacePrefix);
    }

    public boolean checkIfNamespaceExistsByPrefix(String prefix) {
        return repository.existsNamespaceByPrefix(prefix);
    }

    @Transactional
    public Namespace registerProvider(Namespace namespace, Resource resource) throws NamespaceServiceException {
        // Check the provider code is unique within the namespace
        if (!resourceRepository.findByNamespaceIdAndProviderCode(namespace.getId(), resource.getProviderCode()).isEmpty()) {
            throw new NamespaceServiceException(String.format("Namespace '%s', " +
                    "CANNOT REGISTER resource '%s' " +
                    "with provider code '%s', " +
                    "because that PROVIDER CODE ALREADY EXISTS",
                    namespace.getPrefix(),
                    resource.getName(),
                    resource.getProviderCode()));
        }
        return namespace;
    }

    public List<Namespace> getAllNamespaces() {
        return repository.findAll();
    }

    public List<String> getAllNamespacePrefixes() {
        return repository.findAllPrefixes();
    }

    public List<Namespace> findNamespacesModifiedSince(Date start) {
        return repository.findNamespacesModifiedSince(start);
    }
}
