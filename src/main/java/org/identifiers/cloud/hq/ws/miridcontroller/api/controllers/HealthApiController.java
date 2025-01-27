package org.identifiers.cloud.hq.ws.miridcontroller.api.controllers;

import org.identifiers.cloud.hq.ws.miridcontroller.api.models.HealthApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.api.controllers
 * Timestamp: 2019-03-01 10:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This controller offers the endpoints that provide health checks for the service, e.g. liveness and health check
 */
@RestController
@RequestMapping("healthApi")
@Deprecated
@ConditionalOnProperty(name = "management.endpoint.health.enabled",
        matchIfMissing=true, havingValue = "false")
public class HealthApiController {
    // TODO
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
