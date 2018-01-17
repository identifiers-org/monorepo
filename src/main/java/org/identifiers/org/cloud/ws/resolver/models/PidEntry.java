package org.identifiers.org.cloud.ws.resolver.models;

import org.springframework.data.redis.core.RedisHash;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 15:20
 * ---
 */
@RedisHash("pidentries")
public class PidEntry {
    
    private String id;
    private String name;
    private String pattern;
    private String definition;
    private String prefix;
}
