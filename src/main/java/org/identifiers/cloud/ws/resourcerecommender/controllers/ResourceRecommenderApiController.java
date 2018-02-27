package org.identifiers.cloud.ws.resourcerecommender.controllers;

import org.identifiers.cloud.ws.resourcerecommender.models.ResourceRecommenderApiModel;
import org.identifiers.cloud.ws.resourcerecommender.models.ResourceRecommenderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> sortByRecommendationDescending(@RequestBody ResourceRecommenderRequest request) {
        // The model associated with the controller should handle any possible exception that could happen while running
        // the business logic, thus, the controller should handle only exceptions within the domain of the controller.
        return null;
    }

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
