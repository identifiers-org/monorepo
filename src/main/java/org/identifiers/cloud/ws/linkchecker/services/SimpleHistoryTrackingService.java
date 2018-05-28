package org.identifiers.cloud.ws.linkchecker.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.identifiers.cloud.ws.linkchecker.api.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.data.models.TrackedProvider;
import org.identifiers.cloud.ws.linkchecker.data.repositories.LinkCheckResultRepository;
import org.identifiers.cloud.ws.linkchecker.data.repositories.TrackedProviderRepository;
import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

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
    private final static Logger logger = LoggerFactory.getLogger(SimpleHistoryTrackingService.class);
    // Cached stats
    Cache<String, ProviderTracker> providers;
    // Provider trackers by Provider ID
    private ConcurrentMap<String, ProviderTracker> providerTrackers = new ConcurrentHashMap<>();
    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.cache.expiry.seconds}")
    private long cacheExpirySeconds;
    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.cache.size}")
    private long cacheSize;
    // Repositories
    //@Autowired
    private TrackedProviderRepository trackedProviderRepository;
    //@Autowired
    private LinkCheckResultRepository linkCheckResultRepository;

    public SimpleHistoryTrackingService(TrackedProviderRepository trackedProviderRepository,
                                        LinkCheckResultRepository linkCheckResultRepository) {
        providers = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(cacheExpirySeconds, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, ProviderTracker>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, ProviderTracker> removalNotification) {
                        processProviderEviction(removalNotification);
                    }
                })
                .build();
    }

    // Cache Eviction Listener Actions
    private void processProviderEviction(RemovalNotification<String, ProviderTracker> removalNotification) {
        logger.info("Evicting tracking cache for provider ID '{}', URL '{}', EVICTION CAUSE '{}'",
                removalNotification.getKey(),
                removalNotification.getValue().getUrl(),
                removalNotification.getCause().toString());
    }

    // Tracking entry Loaders
    private ProviderTracker loadCreateTrackedProvider(ScoringRequestWithIdPayload scoringRequestWithIdPayload) {
        ProviderTracker providerTracker = new ProviderTracker();
        providerTracker.setId(scoringRequestWithIdPayload.getId())
                .setUrl(scoringRequestWithIdPayload.getUrl())
                .setCreated(new Timestamp(System.currentTimeMillis()));
        Optional<TrackedProvider> trackedProvider = trackedProviderRepository.findById(scoringRequestWithIdPayload
                .getId());
        trackedProvider.ifPresent(entry -> {
            providerTracker.setCreated(entry.getCreated());
        });
        if (!trackedProvider.isPresent()) {
            TrackedProvider newTrackedProvider = new TrackedProvider()
                    .setCreated(providerTracker.getCreated())
                    .setId(providerTracker.getId())
                    .setUrl(providerTracker.getUrl());
            trackedProviderRepository.save(newTrackedProvider);
        }
        return providerTracker;
    }

    @Override
    public ProviderTracker getTrackerForProvider(ScoringRequestWithIdPayload scoringRequestWithIdPayload) throws HistoryTrackingServiceException {
        try {
            return providers.get(scoringRequestWithIdPayload.getId(), new Callable<ProviderTracker>() {
                @Override
                public ProviderTracker call() throws Exception {
                    ProviderTracker providerTracker = loadCreateTrackedProvider(scoringRequestWithIdPayload);
                    // Initialize the stats for the given provider
                    List<LinkCheckResult> linkCheckResults = linkCheckResultRepository.findByProviderId
                            (scoringRequestWithIdPayload.getId());
                    if (linkCheckResults != null) {
                        providerTracker.initHistoryStats(linkCheckResults);
                    }
                    return providerTracker;
                }
            });
        } catch (ExecutionException e) {
            throw new SimpleHistoryTrackingServiceException(String.format("Error while getting scoring stats " +
                    "for Provider ID '%s', URL '%s', because '%s'",
                    scoringRequestWithIdPayload.getId(),
                    scoringRequestWithIdPayload.getUrl(),
                    e.getMessage()));
        }
    }
}
