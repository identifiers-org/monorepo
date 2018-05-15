package org.identifiers.cloud.ws.resolver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.controllers
 * Timestamp: 2018-05-15 13:32
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This controller offers the endpoints that provide health checks for the service, e.g. liveness and health check
 */
public class HealthController {
    // liveness probe
    @RequestMapping(value = "/liveness_check")
    public String livenessCheck() {
        // TODO - This will be refactored out later, it will be the model who will implement the logic to determine
        // TODO - whether the service should be considered "alive" or not, but this code will live here for testing
        // TODO - purposes
        return resolverApiModel.livenessCheck();
    }

    // Readiness check
    @RequestMapping(value = "/readiness_check")
    public String readinessCheck() {
        // TODO - This will be refactored out later, it will be the model who will implement the logic to determine
        // TODO - whether the service should be considered "ready" or not, but this code will live here for testing
        // TODO - purposes
        return resolverApiModel.readinessCheck();
    }

}
