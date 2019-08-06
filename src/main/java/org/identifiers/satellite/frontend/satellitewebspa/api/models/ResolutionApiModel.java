package org.identifiers.satellite.frontend.satellitewebspa.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.api.models
 * Timestamp: 2019-05-15 14:12
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class ResolutionApiModel {
    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.resolver.host}")
    private String resolverHost;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.resolver.port}")
    private String resolverPort;

    // TODO specifying HTTP or HTTPs is not supported by the libapi yet
    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.resolver.schema}")
    private String resolverSchema;

    public ResponseEntity<?> resolveRawCompactIdentifier(String rawCompactIdentifier) {
        ServiceResponseResolve responseResolve =
                ApiServicesFactory.getResolverService(resolverHost, resolverPort)
                        .requestResolutionRawRequest(rawCompactIdentifier);
        if (responseResolve.getHttpStatus().is2xxSuccessful()) {

            // TODO Choose the highest ranking resource
            if (!responseResolve.getPayload().getResolvedResources().isEmpty())
            responseResolve.getPayload().getResolvedResources().sort(new Comparator<ResolvedResource>() {
                @Override
                public int compare(ResolvedResource o1, ResolvedResource o2) {
                    return Integer.compare(o2.getRecommendation().getRecommendationIndex(), o1.getRecommendation().getRecommendationIndex());
                }
            });
            // Return redirect
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.setLocation(new URI(responseResolve.getPayload().getResolvedResources().get(0).getCompactIdentifierResolvedUrl()));
                return new ResponseEntity<>("", headers, HttpStatus.FOUND);
            } catch (URISyntaxException e) {
                String errorMessage = String.format("Compact Identifiers '%s' resolved to provider with internal ID '%d', " +
                        "description '%s', " +
                        "institution '%s', " +
                        "with an INVALID RESOLVED URL '%s'",
                        rawCompactIdentifier,
                        responseResolve.getPayload().getResolvedResources().get(0).getId(),
                        responseResolve.getPayload().getResolvedResources().get(0).getDescription(),
                        responseResolve.getPayload().getResolvedResources().get(0).getInstitution(),
                        responseResolve.getPayload().getResolvedResources().get(0).getCompactIdentifierResolvedUrl());
                log.error(errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(responseResolve.getErrorMessage(), responseResolve.getHttpStatus());
    }
}
