package org.identifiers.cloud.ws.linkchecker.models;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 11:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models a scoring entry, at provider level, within the context of a namespace or prefix, i.e. this entity
 * will be used for tracking the provider home URL.
 */
public class ProviderTracker extends HistoryTracker {
    // Provider ID within the context of a namespace or prefix
    private String id;

    public String getId() {
        return id;
    }

    public ProviderTracker setId(String id) {
        this.id = id;
        return this;
    }
}
