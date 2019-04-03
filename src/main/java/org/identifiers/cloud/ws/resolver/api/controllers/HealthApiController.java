package org.identifiers.cloud.ws.resolver.api.controllers;

import org.identifiers.cloud.ws.resolver.api.models.HealthApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.api.controllers
 * Timestamp: 2018-05-15 13:32
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This controller offers the endpoints that provide health checks for the service, e.g. liveness and health check
 */
@RestController
@RequestMapping("healthApi")
public class HealthApiController {
    @Autowired
    private HealthApiModel model;

    // liveness probe
    @RequestMapping(value = "/liveness_check")
    public String livenessCheck() {
        return model.livenessCheck();
    }

    // Readiness check
    @RequestMapping(value = "/readiness_check")
    public String readinessCheck() {
        return model.readinessCheck();
    }
}
