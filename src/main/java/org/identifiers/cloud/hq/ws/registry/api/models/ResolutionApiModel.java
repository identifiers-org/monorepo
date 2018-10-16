package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.responses.ResolverDatasetPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-10-16 12:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class ResolutionApiModel {
    // Model API
    public ServiceResponseGetResolverDataset getResolverDataset() {
        // Default response
        ServiceResponseGetResolverDataset response = new ServiceResponseGetResolverDataset();
        response.setHttpStatus(HttpStatus.OK);
        response.setPayload(new ResolverDatasetPayload());
        // TODO
        // Return response
        return response;
    }
}
