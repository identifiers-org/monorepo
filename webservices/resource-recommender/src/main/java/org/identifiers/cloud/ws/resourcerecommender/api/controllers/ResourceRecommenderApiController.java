package org.identifiers.cloud.ws.resourcerecommender.api.controllers;

import org.identifiers.cloud.ws.resourcerecommender.api.models.ResourceRecommenderApiModel;
import org.identifiers.cloud.ws.resourcerecommender.api.requests.ServiceRequestRecommend;
import org.identifiers.cloud.ws.resourcerecommender.api.responses.ServiceResponseRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.controllers
 * Timestamp: 2018-02-27 11:04
 * ---
 */
@RestController
public class ResourceRecommenderApiController {

    private final ResourceRecommenderApiModel model;

    public ResourceRecommenderApiController(ResourceRecommenderApiModel model) {
        this.model = model;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> getRecommendations(@RequestBody ServiceRequestRecommend request) {
        // The model associated with the controller should handle any possible exception that could happen while running
        // the business logic, thus, the controller should handle only exceptions within the domain of the controller.
        ServiceResponseRecommend response = model.getRecommendations(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
