package org.identifiers.cloud.hq.ws.miridcontroller.api.models;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.api.models
 * Timestamp: 2019-03-01 10:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class HealthApiModel {
    private static String runningSessionId = UUID.randomUUID().toString();

    public String livenessCheck() {
        return runningSessionId;
    }

    // TODO - Refactor this to actually reflect whether the service ir ready or not
    public String readinessCheck() {
        return runningSessionId;
    }
}
