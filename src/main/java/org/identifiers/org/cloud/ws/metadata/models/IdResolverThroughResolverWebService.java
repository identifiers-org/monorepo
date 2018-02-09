package org.identifiers.org.cloud.ws.metadata.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-08 12:02
 * ---
 */
public class IdResolverThroughResolverWebService implements IdResolver {
    private static Logger logger = LoggerFactory.getLogger(IdResolverThroughResolverWebService.class);

    // Re-try pattern
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500;    // 1.5 seconds

    private static final RetryTemplate

    @Override
    public List<ResolverApiResponseResource> resolve(String compactIdParameter) throws IdResolverException {
        // TODO
        return null;
    }
}
