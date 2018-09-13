package org.identifiers.cloud.ws.resolver.api.controllers;

import org.identifiers.cloud.ws.resolver.api.models.ResolverApiModel;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.controllers
 * Timestamp: 2018-01-15 12:31
 * ---
 */
@RestController
public class ResolverApiController {
    // TODO - Prepare the ground for the endpoints implementing the new use cases
    // TODO - Take this opportunity to refactor the health checks into its own controller

    @Autowired
    private ResolverApiModel resolverApiModel;

    @RequestMapping(value = "{requestString:/.*}", method = RequestMethod.GET)
    public ResponseEntity<?> resolve(@PathVariable String requestString) {
        // TODO
        return new ResponseEntity<>(requestString, HttpStatus.OK);
    }

    //@RequestMapping(value = "{compactId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> queryByCompactId(@PathVariable("compactId") String compactId) {
        // NOTE - I don't like how this looks, if handling exceptions at controller level I think I should go for
        // @ControllerAdvice, but it depends on where the exception belongs to, I think all exceptions belonging to the
        // business logic should be caught and handled at the model level (the main model associated to the controller),
        // and only request related exceptions should be handled at the controller level, probably via @ControllerAdvice
        // mechanism and error controller, that I need to implement anyway.
        ServiceResponse result = resolverApiModel.resolveCompactId(compactId);
        return new ResponseEntity<>(result, result.getHttpStatus());
    }

    //@RequestMapping(value = "{selector}/{compactId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> queryBySelectorAndCompactId(@PathVariable("selector") String selector, @PathVariable("compactId") String compactId) {
        // NOTE - I don't like how this looks, if handling exceptions at controller level I think I should go for
        // @ControllerAdvice, but it depends on where the exception belongs to, I think all exceptions belonging to the
        // business logic should be caught and handled at the model level (the main model associated to the controller),
        // and only request related exceptions should be handled at the controller level, probably via @ControllerAdvice
        // mechanism and error controller, that I need to implement anyway.
        ServiceResponse result = resolverApiModel.resolveCompactId(compactId, selector);
        return new ResponseEntity<>(result, result.getHttpStatus());
    }
}
