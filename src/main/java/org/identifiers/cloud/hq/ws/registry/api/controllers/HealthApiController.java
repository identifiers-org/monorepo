package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.HealthApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2018-11-15 11:51
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
//@RequestMapping("healthApi")
public class HealthApiController {
    @Autowired
    private HealthApiModel model;

    // liveness probe
    @GetMapping(value = {"/healthApi/liveness_check", "/fairapi/health/liveness_check"})
    public String livenessCheck() {
        return model.livenessCheck();
    }

    // Readiness check
    @GetMapping(value = {"/healthApi/readiness_check", "/fairapi/health/readiness_check"})
    public String readinessCheck() {
        return model.readinessCheck();
    }
}