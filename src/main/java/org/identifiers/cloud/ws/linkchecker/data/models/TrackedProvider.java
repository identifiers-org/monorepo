package org.identifiers.cloud.ws.linkchecker.data.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-26 9:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This models a provider being tracked by the service.
 */
public class TrackedProvider implements Serializable {
    // Provider ID within the context of a namespace or prefix
    private String id;
    // A description of this provider within the context of a namespace or prefix
    private String description;
    // Institution information
    private String institution;
    // Location information on this provider within the context of a namespace or prefix, if available
    private String location;
    // Home URL for this provider within the context of a namespace or prefix
    private String url;
    // When the tracking was queued / added to the link checker (UTC)
    private Timestamp created;

}
