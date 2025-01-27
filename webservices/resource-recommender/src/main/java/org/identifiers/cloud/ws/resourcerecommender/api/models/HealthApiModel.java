package org.identifiers.cloud.ws.resourcerecommender.api.models;

import org.identifiers.cloud.ws.resourcerecommender.api.controllers.HealthApiController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.api.models
 * Timestamp: 2018-06-19 16:30
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * Health Check API Controller model.
 * @deprecated in favor of health actuator endpoint
 */
@Component
@Deprecated()
@ConditionalOnBean(HealthApiController.class)
public class HealthApiModel {
    private static final String runningSessionId = UUID.randomUUID().toString();

    public String livenessCheck() {
        return runningSessionId;
    }

    // TODO - Refactor this to actually reflect whether the service ir ready or not
    public String readinessCheck() {
        return runningSessionId;
    }
}
