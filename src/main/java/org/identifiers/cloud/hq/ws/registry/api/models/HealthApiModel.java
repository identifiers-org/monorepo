package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.controllers.HealthApiController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-11-15 11:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Deprecated
@ConditionalOnBean(HealthApiController.class)
public class HealthApiModel {
    private static String runningSessionId = UUID.randomUUID().toString();

    public String livenessCheck() {
        return runningSessionId;
    }

    public String readinessCheck() {
        return runningSessionId;
    }

}
