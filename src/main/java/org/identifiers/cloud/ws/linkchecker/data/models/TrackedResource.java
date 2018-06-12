package org.identifiers.cloud.ws.linkchecker.data.models;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-06-12 13:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This models a resource being tracked by the service. A resource provides information on a given Compact ID, and the
 * URL is a resolved URL given that Compact ID.
 */
@RedisHash("LinkCheckerTrackedResource")
public class TrackedResource implements Serializable {
}
