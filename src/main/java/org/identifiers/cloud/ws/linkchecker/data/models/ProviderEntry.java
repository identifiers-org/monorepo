package org.identifiers.cloud.ws.linkchecker.data.models;

import java.util.List;

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
public class ProviderEntry {
    // Provider ID within the context of a namespace or prefix
    private String id;
    // Home URL for this provider within the context of a namespace or prefix
    private String url;
    // A description of this provider within the context of a namespace or prefix
    private String description;
    // Institution information
    private String institution;
    // Location information on this provider within the context of a namespace or prefix, if available
    private String location;
    // Historical information
    private List<CheckedUrl> history;
}
