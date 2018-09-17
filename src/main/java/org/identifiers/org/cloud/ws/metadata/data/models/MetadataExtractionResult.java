package org.identifiers.org.cloud.ws.metadata.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
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
    // Entry TTL (default initialization to 10 seconds)
    @TimeToLive private Long timeToLive = 10L;
    @Id
    private String id;
    // ID of the resource within the context of this resolution
    @Indexed
    private String resourceId;
    // This is the resolved URL where the metadata will be extracted from
    @Indexed
    private String accessUrl;
    @Indexed
    private String timestamp = (new Timestamp(new Date().getTime())).toString();
    // When this check was requested (UTC)
    private String requestTimestamp;
    // HTTP Status code that came back when retrieving the content of the access URL
    @Indexed
    private int httpStatus;
    // Extracted metadata from the access URL
    private String metadataContent;
    // Optional explanation of a possible error that could have happen during the metadata extraction process
    private String errorMessage;

    public String getResourceId() {
        return resourceId;
    }

    public MetadataExtractionResult setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public MetadataExtractionResult setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
        return this;
    }

    public Timestamp getTimestamp() {
        return Timestamp.valueOf(timestamp);
    }

    public MetadataExtractionResult setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp.toString();
        return this;
    }

    public String getRequestTimestamp() {
        return requestTimestamp;
    }

    public MetadataExtractionResult setRequestTimestamp(String requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public MetadataExtractionResult setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public MetadataExtractionResult setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public MetadataExtractionResult setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public MetadataExtractionResult setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    @Override
    public int compareTo(MetadataExtractionResult o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
