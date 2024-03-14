package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.models.RorIdApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRorApiGetInstitution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-14 17:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This Controller handles requests for working with ROR IDs (<a href="https://ror.org">https://ror.org</a>), in an interoperable way
 */
@RestController
@RequestMapping("rorIdApi")
@Slf4j
public class RorIdApiController {
    @Autowired
    private RorIdApiModel model;

    // TODO
    @PostMapping(value = "/getInstitutionForRorId")
    public ResponseEntity<?> getInstitutionForRorId(@RequestBody ServiceRequestRorApiGetInstitution request) {
        return model.getInstitutionForRorId(request.getPayload().getRorId());
    }
}
