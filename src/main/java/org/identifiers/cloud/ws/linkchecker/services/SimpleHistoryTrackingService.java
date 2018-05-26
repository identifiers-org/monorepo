package org.identifiers.cloud.ws.linkchecker.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.identifiers.cloud.ws.linkchecker.api.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.ws.linkchecker.data.repositories.TrackedProviderRepository;
import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
    @Autowired
    private TrackedProviderRepository trackedProviderRepository;

    public SimpleHistoryTrackingService() {
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
        logger.info("Evicting tracking cache for provider ID '{}', EVICTION CAUSE '{}'",
                removalNotification.getKey(),
                removalNotification.getCause().toString());
    }

    // Cached Stats Data Loaders
    private ProviderTracker loadTrackedProvider(ScoringRequestWithIdPayload scoringRequestWithIdPayload) {
        ProviderTracker providerTracker = new ProviderTracker();
        providerTracker.setId(scoringRequestWithIdPayload.getId())
                .setUrl(scoringRequestWithIdPayload.getUrl())
                .setCreated(new Timestamp(System.currentTimeMillis()));
        trackedProviderRepository.findById(scoringRequestWithIdPayload.getId()).ifPresent(trackedProvider -> {
            providerTracker.setCreated(trackedProvider.getCreated());
        });
        return providerTracker;
    }

    @Override
    public ProviderTracker getTrackerForProvider(ScoringRequestWithIdPayload scoringRequestWithIdPayload) {
        ProviderTracker providerTracker = new ProviderTracker();
        providerTracker.setId(scoringRequestWithIdPayload.getId())
                .setUrl(scoringRequestWithIdPayload.getUrl())
                .setCreated(new Timestamp(System.currentTimeMillis()));
        try {
            return providers.get(scoringRequestWithIdPayload.getId(), new Callable<ProviderTracker>() {
                @Override
                public ProviderTracker call() throws Exception {
                    // TODO - Load from Repo
                    return providerTracker;
                }
            });
        } catch (ExecutionException e) {
            return providerTracker;
        }
    }
}
