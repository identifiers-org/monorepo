package org.identifiers.cloud.ws.resolver.api.models;

import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.models.ResolverDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-05-16 13:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * Main model for Insight API Controller.
 */
@Component
@Scope("prototype")
public class InsightApiModel {
    @Autowired
    private ResolverDataHelper resolverDataHelper;

    // NOTE - This is fine, don't panic ^_^
    public ServiceResponse<ResponseResolvePayload> getAllSampleIdsResolved() {
        // Prepare default answer

        var resolvedResources = resolverDataHelper.resolveAllResourcesWithTheirSampleId();
        var payload = new ResponseResolvePayload().setResolvedResources(resolvedResources);

        if (payload.getResolvedResources().isEmpty()) {
            return ServiceResponse.ofError(
                HttpStatus.NOT_FOUND,
                "NO PROVIDERS found in the Resolution Service data set."
            );
        } else {
            return ServiceResponse.of(payload);
        }
    }

    public ServiceResponse<ResponseResolvePayload> getAllHomeUrls() {
        return getAllSampleIdsResolved();
    }
}
