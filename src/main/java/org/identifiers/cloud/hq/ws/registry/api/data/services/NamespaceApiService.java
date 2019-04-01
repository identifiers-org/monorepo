package org.identifiers.cloud.hq.ws.registry.api.data.services;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiDataModelHelper;
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
            // Locate the resources within the namespace
            // NOTE - There must be another way of doing this model mappings
            // TODO - Refactor model transformations into an external helper
            List<Resource> resources = new ArrayList<>();
            resources = resourceRepository.findAllByNamespaceId(namespace.getId()).parallelStream()
                    .map(ApiDataModelHelper::getResourceFrom).collect(Collectors.toList());
            return ApiDataModelHelper.getNamespaceFrom(namespace)
                    .setResources(resources);
        }).collect(Collectors.toList());
    }
}
