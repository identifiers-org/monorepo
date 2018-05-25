package org.identifiers.cloud.ws.linkchecker.services;

import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.services
 * Timestamp: 2018-05-25 11:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class DefaultHistoryTrackingService implements HistoryTrackingService {
    @Override
    public ProviderTracker getTrackerForProvider(String providerId) {
        return null;
    }
}
