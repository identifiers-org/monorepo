package org.identifiers.cloud.ws.linkchecker.api.models;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.models
 * Timestamp: 2018-06-14 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * Health Check API Controller model.
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
