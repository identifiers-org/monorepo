package org.identifiers.org.cloud.ws.metadata.controllers;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.controllers
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@RestController
public class MetadataApiController {

    private MetadataApiController metadataApiController;

    public MetadataApiController(MetadataApiController metadataApiController) {
        this.metadataApiController = metadataApiController;
    }
}
