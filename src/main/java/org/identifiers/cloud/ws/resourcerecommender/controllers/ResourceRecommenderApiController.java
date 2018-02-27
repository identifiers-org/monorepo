package org.identifiers.cloud.ws.resourcerecommender.controllers;

import org.identifiers.cloud.ws.resourcerecommender.models.ResourceRecommenderApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.controllers
 * Timestamp: 2018-02-27 11:04
 * ---
 */
public class ResourceRecommenderApiController {

    @Autowired
    private ResourceRecommenderApiModel model;

    // liveness probe
    @RequestMapping(value = "/liveness_check")
    public String livenessCheck() {
        // TODO - This will be refactored out later, it will be the model who will implement the logic to determine
        // TODO - whether the service should be considered "alive" or not, but this code will live here for testing
        // TODO - purposes
        return model.livenessCheck();
    }

    // Readiness check
    @RequestMapping(value = "/readiness_check")
    public String readinessCheck() {
        // TODO - This will be refactored out later, it will be the model who will implement the logic to determine
        // TODO - whether the service should be considered "ready" or not, but this code will live here for testing
        // TODO - purposes
        return model.readinessCheck();
    }

}
