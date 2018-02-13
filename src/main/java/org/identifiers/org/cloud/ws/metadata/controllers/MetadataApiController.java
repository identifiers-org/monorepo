package org.identifiers.org.cloud.ws.metadata.controllers;

import org.identifiers.org.cloud.ws.metadata.models.MetadataApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.controllers
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@RestController
public class MetadataApiController {

    @Autowired
    private MetadataApiModel metadataApiModel;

    @RequestMapping(value = "{compactId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getMetadataFor(@PathVariable("compactId") String compactIdParameter) {
        // TODO
        return new ResponseEntity<>(String.format("Thanks for the Compact ID '%s'", compactIdParameter), HttpStatus.OK);
    }
}
