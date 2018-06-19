package org.identifiers.cloud.ws.resourcerecommender.api.controllers;

import org.identifiers.cloud.ws.resourcerecommender.api.models.ResourceRecommenderApiModel;
import org.identifiers.cloud.ws.resourcerecommender.api.requests.ServiceRequestRecommend;
import org.identifiers.cloud.ws.resourcerecommender.api.responses.ServiceResponseRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.controllers
 * Timestamp: 2018-02-27 11:04
 * ---
 */
@RestController
public class ResourceRecommenderApiController {

    @Autowired
    private ResourceRecommenderApiModel model;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> getRecommendations(@RequestBody ServiceRequestRecommend request) {
        // The model associated with the controller should handle any possible exception that could happen while running
        // the business logic, thus, the controller should handle only exceptions within the domain of the controller.
        // TODO - This is where we check the API version?
        ServiceResponseRecommend response = model.getRecommendations(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
