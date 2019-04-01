package org.identifiers.cloud.hq.ws.registry.api.data.services;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.services
 * Timestamp: 2019-03-06 14:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a service for working with Namespace model within the API models context, i.e. it works with API Namespace
 * model
 */
@Component
public class NamespaceApiService {
    // Repositories
    @Autowired
    private NamespaceRepository namespaceRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    // END - Repositories

    public List<Namespace> getNamespaceTreeDownToLeaves() {
        return namespaceRepository.findAll().parallelStream().map(namespace -> {
            // TODO
            // TODO - Locate the resources within the namespace
            List<Resource> resources = new ArrayList<>();
            return new Namespace()
                    .setId(namespace.getId())
                    .setPrefix(namespace.getPrefix())
                    .setName(namespace.getName())
                    .setPattern(namespace.getPattern())
                    .setDescription(namespace.getDescription())
                    .setCreated(namespace.getCreated())
                    .setModified(namespace.getModified())
                    .setSampleId(namespace.getSampleId())
                    .setResources(resources);
        }).collect(Collectors.toList());
    }
}
