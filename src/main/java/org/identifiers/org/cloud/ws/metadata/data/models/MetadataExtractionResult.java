package org.identifiers.org.cloud.ws.metadata.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.data.models
 * Timestamp: 2018-09-16 14:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RedisHash(value = "MetadataMetadataExtractionResult")
public class MetadataExtractionResult implements Serializable, Comparable<MetadataExtractionResult> {
    // TODO
    @Id private String id;
    @Indexed private String resourceId;
    @Indexed private String accessUrl;
    @Indexed private String timestamp = (new Timestamp(new Date().getTime())).toString();
    // When this check was requested (UTC)
    private String requestTimestamp;
    @Indexed private int httpStatus;
    private String metadataContent;
    private String errorMessage;
}
