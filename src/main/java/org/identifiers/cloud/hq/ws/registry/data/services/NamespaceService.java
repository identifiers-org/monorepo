package org.identifiers.cloud.hq.ws.registry.data.services;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2018-10-16 14:33
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
// TODO - Affected by relational refactoring
public class NamespaceService {
    @Autowired
    private NamespaceRepository namespaceRepository;

    public List<Namespace> getNamespaceTreeDownToLeaves() {
        return namespaceRepository.findAll();
    }
}
