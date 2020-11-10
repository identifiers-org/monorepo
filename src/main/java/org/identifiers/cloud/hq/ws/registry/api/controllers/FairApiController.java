package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.FairApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.FairApiCoveragePayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.FairApiCoverageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: idorg-hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2020-11-10 09:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * FAIR API Controller, according to the documentation and specification at
 * https://fairapi.org
 */
@RestController
@RequestMapping("fairapi")
public class FairApiController {
    @Autowired
    private FairApiModel model;

    // LUI coverage endpoint
    @PostMapping(value = "/coverage/checkForLui")
    public ResponseEntity<?> checkForLui(@RequestBody FairApiCoveragePayload payload) {
        FairApiCoverageResponse response = model.checkForLui(payload);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    // TODO - Interoperability Endpoint - getCompactIdentifier(namespace, lui)
    // TODO - Interoperability Endpoint - getInteroperabilityUrl(namespace, lui)
}
