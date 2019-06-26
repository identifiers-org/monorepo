package org.identifiers.cloud.ws.resolver.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.resolver.api.ApiCentral;
import org.identifiers.cloud.ws.resolver.api.responses.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.api.responses.ServiceResponseResolve;
import org.identifiers.cloud.ws.resolver.models.CompactId;
import org.identifiers.cloud.ws.resolver.models.CompactIdException;
import org.identifiers.cloud.ws.resolver.models.CompactIdParsingHelper;
import org.identifiers.cloud.ws.resolver.models.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.resolver.services.ResolutionService;
import org.identifiers.cloud.ws.resolver.services.ResolutionServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 10:31
 * ---
 */
// Well, when using Spring Boot, you have to fit in a particular way of doing things. Is it the right way?
@Component
@Scope("prototype")
@Slf4j
public class ResolverApiModel {
    // External Helpers
    @Autowired
    private CompactIdParsingHelper compactIdParsingHelper;
    @Autowired
    private ResolutionService resolutionService;

    // Helpers
    private ServiceResponseResolve createDefaultResponse() {
        return (ServiceResponseResolve)
                new ServiceResponseResolve()
                .setApiVersion(ApiCentral.apiVersion)
                .setPayload(new ResponseResolvePayload().setResolvedResources(new ArrayList<>()));
    }

    @Deprecated
    private CompactId getCompactIdentifier(String compactId, ServiceResponseResolve response) {
        try {
            return new CompactId(compactId);
        } catch (CompactIdException e) {
            response.setErrorMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    // END - Helpers

    // --- Resolution API ---
    public ServiceResponseResolve resolveRawCompactId(String rawCompactId) {
        // This is the only entry method right now
        ServiceResponseResolve response = createDefaultResponse();
        ParsedCompactIdentifier parsedCompactIdentifier = compactIdParsingHelper.parseCompactIdRequest(rawCompactId);
        ResolutionServiceResult resolutionServiceResult = resolutionService.resolve(parsedCompactIdentifier);
        if (resolutionServiceResult.isResolved()) {
            if (resolutionServiceResult.getResolvedResources().isEmpty()) {
                // Resolved but there are no resources
                response.setErrorMessage(resolutionServiceResult.getErrorMessage());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            } else {
                // Resolved with resources
                response.getPayload().setResolvedResources(resolutionServiceResult.getResolvedResources());
            }
            // Return response from resolver
            return response;
        }
        // Default error response
        String errorMessage = String.format("INVALID resolution request for '%s', due to '%s'", rawCompactId, resolutionServiceResult.getErrorMessage());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
