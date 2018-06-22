package org.identifiers.cloud.ws.resolver.api.controllers;

import org.identifiers.cloud.ws.resolver.models.InsightApiModel;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponseResolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.api.controllers
 * Timestamp: 2018-05-16 13:16
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Expose an API that offers insights into the resolver dataset.
 */
@RestController
public class InsightApiController {
    @Autowired
    private InsightApiModel model;

    @RequestMapping(value = "/get_all_sample_ids_resolved")
    public ResponseEntity<?> getAllSampleIdsResolved() {
        ServiceResponseResolve response = model.getAllSampleIdsResolved();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @RequestMapping(value = "/get_all_home_urls")
    public ResponseEntity<?> getAllHomeUrls() {
        ServiceResponseResolve response = model.getAllHomeUrls();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
