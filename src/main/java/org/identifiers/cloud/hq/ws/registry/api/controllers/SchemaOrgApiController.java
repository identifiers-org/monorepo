package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-17 06:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("schemaOrgApi")
public class SchemaOrgApiController {
    // TODO

    @GetMapping("getPlatformAnnotation")
    public ResponseEntity<?> getPlatformAnnotation() {
        // TODO
        return new ResponseEntity<>("This is the annotation for the general platform", HttpStatus.OK);
    }
}
