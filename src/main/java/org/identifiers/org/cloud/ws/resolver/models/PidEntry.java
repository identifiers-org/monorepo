package org.identifiers.org.cloud.ws.resolver.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 15:20
 * ---
 */
@RedisHash("pidentries")
public class PidEntry {
    @Id String id;
    String name;
    String pattern;
    String definition;
    @Indexed String prefix;
}
