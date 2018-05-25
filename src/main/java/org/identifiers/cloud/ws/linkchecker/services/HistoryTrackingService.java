package org.identifiers.cloud.ws.linkchecker.services;

import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.services
 * Timestamp: 2018-05-25 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * A history tracking service maintains and provides the tracking information for the requested entity.
 */
public interface HistoryTrackingService {
    ProviderTracker getTrackerForProvider(String providerId);
}
