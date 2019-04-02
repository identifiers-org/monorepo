package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-27 9:04
 * ---
 */
public interface ResolverDataFetcher {
    List<ResourceEntry> findResourcesByPrefix(String prefix);

    Iterable<Namespace> findAllPidEntries();
}
