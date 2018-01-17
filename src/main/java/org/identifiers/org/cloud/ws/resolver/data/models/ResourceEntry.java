package org.identifiers.org.cloud.ws.resolver.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 16:15
 * ---
 */
@RedisHash("resourceEntries")
public class ResourceEntry {
    @Id
    private String id;
    private String accessUrl;
    private String info;
    private String institution;
    private String location;
    private boolean official;
    @Indexed
    private String resourcePrefix;
    private String localId;
    private String testString;
}
