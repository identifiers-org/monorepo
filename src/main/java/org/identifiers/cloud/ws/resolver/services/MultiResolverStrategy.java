package org.identifiers.cloud.ws.resolver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.services
 * Timestamp: 2019-06-26 15:31
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the common base class for resolution services that are based on composing resolutions from other resolvers.
 */
public abstract class MultiResolverStrategy implements ResolutionService {
    @Autowired
    @Qualifier("RegistryNamespaceResolver")
    private ResolutionService registryNamespaceResolver;

    protected List<ResolutionService> getResolverChain() {
        return Arrays.asList(
                registryNamespaceResolver
        );
    }
}
