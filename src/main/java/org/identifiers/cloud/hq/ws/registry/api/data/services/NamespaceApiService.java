package org.identifiers.cloud.hq.ws.registry.api.data.services;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        // TODO
        return new ArrayList<>();
    }
}
