package org.identifiers.org.cloud.ws.metadata.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:44
 * ---
 */
public interface IdResolver {
    ResolverApiResponse resolve(String compactIdParameter);
}
