package org.identifiers.cloud.ws.resolver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Component
public class MultiResolverBuilder {
    @Autowired
    @Qualifier("RegistryNamespaceResolver")
    private ResolutionService registryNamespaceResolver;

    @Autowired
    @Qualifier("CompactIdentifierResolutionService")
    private ResolutionService compactIdentifierResolver;

    protected List<ResolutionService> getResolverChain() {
        return Arrays.asList(
                registryNamespaceResolver,
                compactIdentifierResolver
        );
    }
}
