package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.models.RorIdApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
@Slf4j
public class RorIdApiController {
    @Autowired
    private RorIdApiModel model;

    // TODO
    @PostMapping(value = "/getInstitutionForRorId")
    public ResponseEntity<?> getInstitutionForRorId(@PathVariable String rorId, HttpServletRequest request) {
        return model.getInstitutionForRorId(rorId);
    }
}
