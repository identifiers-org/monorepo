package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.DevAuthApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-20 14:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This controller is for development purposes around the authentication subsystem
 */
@RestController
@RequestMapping("devAuthApi")
public class DevAuthApiController {
    @Autowired
    private DevAuthApiModel model;

    @GetMapping("getPrincipal")
    public ResponseEntity<?> getPrincipal() {
        return model.getPrincipal();
    }

    @GetMapping("getUsername")
    public ResponseEntity<?> getUsername() {
        return model.getUsername();
    }
}
