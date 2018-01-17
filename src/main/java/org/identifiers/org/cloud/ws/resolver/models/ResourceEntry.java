package org.identifiers.org.cloud.ws.resolver.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 16:15
 * ---
 */
@RedisHash("resourceEntries")
public class ResourceEntry {
    @Id String id;
    String accessUrl;
}
