package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.ResolutionApiHelper;
import org.identifiers.cloud.hq.ws.registry.api.responses.ResolverDatasetPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private NamespaceService namespaceService;

    // Model API
    public ServiceResponseGetResolverDataset getResolverDataset() {
        // Default response
        ServiceResponseGetResolverDataset response = new ServiceResponseGetResolverDataset();
        response.setHttpStatus(HttpStatus.OK);
        response.setPayload(new ResolverDatasetPayload());
        // TODO
        response.getPayload().setNamespaces(ResolutionApiHelper.getResolutionDatasetFrom(namespaceService.getNamespaceTreeDownToLeaves()));
        // Return response
        return response;
    }
}
