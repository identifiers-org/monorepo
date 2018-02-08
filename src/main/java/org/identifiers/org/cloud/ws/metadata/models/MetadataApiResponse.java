package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.http.HttpStatus;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-07 11:36
 * ---
 */
public class MetadataApiResponse {

    private HttpStatus httpStatus = HttpStatus.OK;
    private String errorMessage;
    // Right now we focus on JSON-LD formatted metadata and we're not doing anything with it, so I keep it as a String
    private String metadata;
}
