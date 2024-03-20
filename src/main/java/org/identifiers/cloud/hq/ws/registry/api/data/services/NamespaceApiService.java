package org.identifiers.cloud.hq.ws.registry.api.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ApiAndDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
@Slf4j
@Component
public class NamespaceApiService {
    private final NamespaceRepository namespaceRepository;
    private final ResourceRepository resourceRepository;
    public NamespaceApiService(NamespaceRepository namespaceRepository,
                               ResourceRepository resourceRepository) {
        this.namespaceRepository = Objects.requireNonNull(namespaceRepository);
        this.resourceRepository = Objects.requireNonNull(resourceRepository);
    }

    public List<Namespace> getNamespaceTreeDownToLeaves() {
        List<Namespace> namespaces = namespaceRepository
                .findAll().stream()
                .map(ApiAndDataModelsHelper::getNamespaceFrom)
                .toList();
        var resources = resourceRepository.findAll().parallelStream()
                .collect(Collectors.groupingBy(Resource::getNamespaceId));

        namespaces.parallelStream().forEach(namespace -> {
            long namespaceId = namespace.getId();
            var namespaceResources = resources
                    .get(namespaceId)
                    .stream()
                    .map(ApiAndDataModelsHelper::getResourceFrom)
                    .toList();
            if (namespaceResources.isEmpty()) {
                log.error("No resources found for namespace {}",
                        namespace.getPrefix());
            }
            namespace.setResources(namespaceResources);
        });

        return namespaces;
    }
}
