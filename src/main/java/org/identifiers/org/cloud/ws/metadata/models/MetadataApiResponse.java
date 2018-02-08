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
}
