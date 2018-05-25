package org.identifiers.cloud.ws.linkchecker.services;

import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.services
 * Timestamp: 2018-05-25 11:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class SimpleHistoryTrackingService implements HistoryTrackingService {
    @Override
    public ProviderTracker getTrackerForProvider(String providerId) {
        return null;
    }
}
