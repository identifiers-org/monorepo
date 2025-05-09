package org.identifiers.satellite.frontend.satellitewebspa.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.satellite.frontend.satellitewebspa.api.exceptions.FailedResolutionException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

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
    public RedirectView resolveRawCompactIdentifier(ServiceResponse<ResponseResolvePayload> responseResolve) {
        if (responseResolve.getHttpStatus().is2xxSuccessful()) {
            String location;
            if (!responseResolve.getPayload().getResolvedResources().isEmpty()) {
                // We have resources
                if (responseResolve.getPayload().getParsedCompactIdentifier().isDeprecatedNamespace()) {
                    // The namespace is deprecated, we just choose among its resources, just in case they're all not deprecated, we sort them
                    responseResolve.getPayload().getResolvedResources().sort((o1, o2) -> Integer.compare(o2.getRecommendation().getRecommendationIndex(), o1.getRecommendation().getRecommendationIndex()));
                    if (responseResolve.getPayload().getParsedCompactIdentifier().isRenderDeprecatedLanding() ||
                            responseResolve.getPayload().getResolvedResources().get(0).isRenderDeprecatedLanding()) {
                        location = "/deactivatedLanding/" + responseResolve.getPayload().getParsedCompactIdentifier().getRawRequest();
                        log.info("Resolving to deactivated landing");
                    } else {
                        location = responseResolve.getPayload().getResolvedResources().get(0).getCompactIdentifierResolvedUrl();
                        log.info("Resolving to {}", location);
                    }
                } else {
                    List<ResolvedResource> resolvedResources = responseResolve
                            .getPayload().getResolvedResources();
                    boolean allResourcesDeprecated = resolvedResources.stream()
                            .allMatch(ResolvedResource::isDeprecatedResource);
                    if (!allResourcesDeprecated) {
                        // When at least one resource is not deprecated, use non deprecated ones
                        //   This is important for resolving queries for deprecated provider codes
                        resolvedResources = resolvedResources.stream()
                                .filter(resolvedResource ->
                                        !resolvedResource.isDeprecatedResource()
                                ).collect(Collectors.toList());
                    }
                    if (!resolvedResources.isEmpty()) {
                        // We sort them and choose the highest ranking one
                        resolvedResources.sort((o1, o2) ->
                                Integer.compare(o2.getRecommendation().getRecommendationIndex(),
                                        o1.getRecommendation().getRecommendationIndex()));
                        ResolvedResource resolvedResource = resolvedResources.get(0);
                        if (resolvedResource.isDeprecatedResource() && resolvedResource.isRenderDeprecatedLanding()) {
                            location = "/deactivatedLanding/" + responseResolve
                                    .getPayload().getParsedCompactIdentifier().getRawRequest();
                        } else if (resolvedResource.isProtectedUrls() && resolvedResource.isRenderProtectedLanding()) {
                            location = "/protectedLanding/" + responseResolve
                                    .getPayload().getParsedCompactIdentifier().getRawRequest();
                        } else {
                            location = resolvedResource.getCompactIdentifierResolvedUrl();
                        }
                        log.info("Resolving to {}", location);
                    } else {
                        String errorMessage = String.format("Namespace '%s' is ACTIVE but ALL ITS RESOURCES ARE DEPRECATED",
                                responseResolve.getPayload().getParsedCompactIdentifier().getNamespace());
                        log.error(errorMessage);
                        throw new FailedResolutionException(errorMessage);
                    }
                }
                return new RedirectView(location);
            }
            throw new FailedResolutionException(String.format("Zero resources were found for %s",
                    responseResolve.getPayload().getParsedCompactIdentifier().getRawRequest()));
        }
        throw new FailedResolutionException(responseResolve.getErrorMessage());
//        return new ResponseEntity<>(responseResolve.getErrorMessage(), responseResolve.getHttpStatus());
    }
}
