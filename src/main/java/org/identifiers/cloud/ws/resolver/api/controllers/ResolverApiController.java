package org.identifiers.cloud.ws.resolver.api.controllers;

import javafx.util.Pair;
import org.identifiers.cloud.ws.resolver.api.models.ResolverApiModel;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.controllers
 * Timestamp: 2018-01-15 12:31
 * ---
 */
@RestController
public class ResolverApiController {
    private static final Logger logger = LoggerFactory.getLogger(ResolverApiController.class);

    @Autowired
    private ResolverApiModel resolverApiModel;

    // Compact Identifier and provider code helper
    private Pair<String, String> extractProviderAndCompactIdentifier(String resolutionRequest) {
        String provider = null;
        String compactIdentifier = null;
        if (resolutionRequest.contains((":"))) {
            // We divide based on ':' and first '/'
            String[] splitByColon = resolutionRequest.split(":");
            if (splitByColon[0].contains("/")) {
                provider = splitByColon[0].split("/")[0];
                compactIdentifier = resolutionRequest
                        .replaceFirst(provider, "")
                        .replaceFirst("/", "");
            } else {
                compactIdentifier = resolutionRequest;
            }
        } else if (resolutionRequest.contains("/")) {
            // We look for the first '/' to find the provider code
            provider = resolutionRequest.split("/")[0];
            compactIdentifier = compactIdentifier = resolutionRequest
                    .replaceFirst(provider, "")
                    .replaceFirst("/", "");
        } else {
            // This case is very likely to be a not valid Compact Identifier
            compactIdentifier = resolutionRequest;
        }
        logger.info("For resolution request '{}', found provider code '{}' and compact identifier '{}'",
                resolutionRequest, provider, compactIdentifier);
        return new Pair<>(provider, compactIdentifier);
    }

    @RequestMapping(value = "/{resolutionRequest}/**", method = RequestMethod.GET)
    public ResponseEntity<?> resolve(@PathVariable String resolutionRequest, HttpServletRequest request) {
        // TODO
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        logger.info("Resolution request, PATH '{}' and best matching pattern '{}'", path, bestMatchingPattern);
        Pair<String, String> providerAndCompactIdentifier = extractProviderAndCompactIdentifier(path.replaceFirst("/", ""));
        ServiceResponse result = null;
        if (providerAndCompactIdentifier.getKey() != null) {
            result = resolverApiModel.resolveCompactId(providerAndCompactIdentifier.getValue(),
                    providerAndCompactIdentifier.getKey());
        } else {
            result = resolverApiModel.resolveCompactId(providerAndCompactIdentifier.getValue());
        }
        return new ResponseEntity<>(result, result.getHttpStatus());
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
