package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.ResolutionApiModel;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2018-10-16 12:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("resolutionApi")
public class ResolutionApiController {
    @Autowired
    private ResolutionApiModel model;

    @RequestMapping(value = "getResolverDataset", method = RequestMethod.GET)
    public ResponseEntity<?> getResolverDataset() {
        ServiceResponseGetResolverDataset response = model.getResolverDataset();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
