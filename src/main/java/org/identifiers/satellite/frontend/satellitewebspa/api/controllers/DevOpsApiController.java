package org.identifiers.satellite.frontend.satellitewebspa.api.controllers;

import org.identifiers.satellite.frontend.satellitewebspa.api.models.DevOpsApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.api.controllers
 * Timestamp: 2019-05-15 13:13
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("devops")
public class DevOpsApiController {
    @Autowired
    private DevOpsApiModel model;

    @GetMapping("/getSpaConfiguration")
    public ResponseEntity<?> getSpaConfiguration() {
        return model.getSpaConfiguration();
    }
}
