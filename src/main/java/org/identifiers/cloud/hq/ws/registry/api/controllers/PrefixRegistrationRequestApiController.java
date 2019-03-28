package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.PrefixRegistrationRequestApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-03-28 15:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This controller supports the API that handles prefix registration requests
 */
@RestController
@RequestMapping(value = "prefixRegistrationApi")
public class PrefixRegistrationRequestApiController {
    // TODO
    @Autowired
    private PrefixRegistrationRequestApiModel model;

    @PostMapping(value = "/registerPrefix")
    public ResponseEntity<?> registerPrefix(@RequestBody ServiceRequestRegisterPrefix request) {
        // TODO Model delegation
        return new ResponseEntity<>("registerPrefix()", HttpStatus.OK);
    }
}
