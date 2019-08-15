package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.RorIdApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-14 17:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Controller handles requests for working with ROR IDs (https://ror.org), in an interoperable way
 */
@RestController
@RequestMapping("rorIdApi")
public class RorIdApiController {
    @Autowired
    private RorIdApiModel model;

    // TODO
    @GetMapping(value = "/getInstitutionForRorId/{rorId}")
    public ResponseEntity<?> getInstitutionForRorId(@PathVariable String rorId) {
        //ServiceResponseDeactivateResource response = model.deactivateResource(resourceId);
        //return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("", HttpStatus.NOT_IMPLEMENTED);
    }
}
