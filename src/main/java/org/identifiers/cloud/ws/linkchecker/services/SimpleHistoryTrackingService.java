package org.identifiers.cloud.ws.linkchecker.services;

import com.google.common.cache.*;
import org.identifiers.cloud.ws.linkchecker.models.ProviderTracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
    // Provider trackers by Provider ID
    private ConcurrentMap<String, ProviderTracker> providerTrackers = new ConcurrentHashMap<>();

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.cache.expiry.seconds}")
    private long cacheExpirySeconds;

    @Value("${org.identifiers.cloud.ws.linkchecker.backend.data.cache.size}")
    private long cacheSize;

    private void processCacheEntryRemoval(RemovalNotification<String, ProviderTracker> removalNotification) {
        // TODO
    }

    // Cached stats
    LoadingCache<String, ProviderTracker> providers;

    public SimpleHistoryTrackingService() {
        providers = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(cacheExpirySeconds, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, ProviderTracker>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, ProviderTracker> removalNotification) {
                        // TODO
                    }
                })
                .build(new CacheLoader<String, ProviderTracker>() {
                    @Override
                    public ProviderTracker load(String s) throws Exception {
                        // TODO
                        return null;
                    }
                });
    }

    @Override
    public ProviderTracker getTrackerForProvider(String providerId) {
        try {
            return providers.get(providerId);
        } catch (ExecutionException e) {
            // TODO
            return null;
        }
    }
}
