package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.models.api.responses.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.models.api.responses.ServiceResponseResolve;
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
    // TODO
    @Autowired
    private ResolverDataHelper resolverDataHelper;

    // NOTE - This is fine, don't panic ^_^
    public ServiceResponseResolve getAllSampleIdsResolved() {
        // Prepare default answer
        ServiceResponseResolve resolverApiResponse = new ServiceResponseResolve();
        // TODO - Split this with a default value and take care of any possible exception
        resolverApiResponse
                .setPayload(new ResponseResolvePayload()
                        .setResolvedResources(resolverDataHelper.resolveAllResourcesWithTheirSampleId()));
        if (resolverApiResponse.getPayload().getResolvedResources().isEmpty()) {
            resolverApiResponse.setErrorMessage("NO PROVIDERS found in the Resolution Service data set.");
            resolverApiResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        resolverApiResponse.setApiVersion(ApiCentral.apiVersion);
        return resolverApiResponse;
    }

    public ServiceResponseResolve getAllHomeUrls() {
        // TODO
        return null;
    }
}
