package org.identifiers.org.cloud.ws.metadata.models.api.responses;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models.api.responses
 * Timestamp: 2018-03-07 10:59
 * ---
 */
public class ResponseFetchMetadataPayload implements Serializable {
    String metadata;

    public String getMetadata() {
        return metadata;
    }

    public ResponseFetchMetadataPayload setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }
}
