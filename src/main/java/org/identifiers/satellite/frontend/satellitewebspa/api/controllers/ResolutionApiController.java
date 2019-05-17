package org.identifiers.satellite.frontend.satellitewebspa.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.satellite.frontend.satellitewebspa.api.models.ResolutionApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.api.controllers
 * Timestamp: 2019-05-16 10:19
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
//@RestController
@RestController
@RequestMapping("resolutionApi")
@Slf4j
public class ResolutionApiController {
    @Autowired
    private ResolutionApiModel model;

    // TODO

    @RequestMapping(value = "/{resolutionRequest}/**", method = RequestMethod.GET)
    public ResponseEntity<?> resolveRawCompactIdentifier(@PathVariable String resolutionRequest, HttpServletRequest request) {
        // Extract the request path
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        log.info("Resolution request, PATH '{}'", path);
        // Resolve
        return model.resolveRawCompactIdentifier(path.replaceFirst("/resolutionApi/", ""));
    }
}
