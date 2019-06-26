package org.identifiers.cloud.ws.resolver.services;

import org.identifiers.cloud.ws.resolver.models.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.resolver.models.ResolvedResource;

import java.util.List;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.services
 * Timestamp: 2019-06-26 14:59
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface defines what Compact Identifier Resolution service must honor as its contract.
 *
 * The reason why this comes to life is due to the introduction of additional resolution mechanisms to the one that is
 * based on the registry
 */
public interface ResolutionService {

    List<ResolvedResource> resolve(ParsedCompactIdentifier parsedCompactIdentifier);
}
