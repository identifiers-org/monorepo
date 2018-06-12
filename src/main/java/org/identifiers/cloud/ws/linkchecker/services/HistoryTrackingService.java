package org.identifiers.cloud.ws.linkchecker.services;

import org.identifiers.cloud.ws.linkchecker.api.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.models.HistoryTracker;
import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;
import org.identifiers.cloud.ws.linkchecker.models.ResourceTracker;

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
    ProviderTracker getTrackerForProvider(ScoringRequestWithIdPayload scoringRequestWithIdPayload) throws HistoryTrackingServiceException ;
    ResourceTracker getTrackerForResource(ScoringRequestWithIdPayload scoringRequestWithIdPayload) throws HistoryTrackingServiceException ;
    // TODO - Add the methods for the rest of the entities
    HistoryTracker updateTrackerWith(LinkCheckResult linkCheckResult) throws HistoryTrackingServiceException;
}
