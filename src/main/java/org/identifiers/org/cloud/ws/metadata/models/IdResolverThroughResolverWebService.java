package org.identifiers.org.cloud.ws.metadata.models;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-08 12:02
 * ---
 */
public class IdResolverThroughResolverWebService implements IdResolver {
    // Re-try pattern
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    
    @Override
    public List<ResolverApiResponseResource> resolve(String compactIdParameter) throws IdResolverException {
        // TODO
        return null;
    }
}
