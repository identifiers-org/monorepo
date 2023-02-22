package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.data.services.NamespaceApiService;
import org.identifiers.cloud.hq.ws.registry.api.responses.ResolverDatasetPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
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
    private NamespaceApiService namespaceApiService;

    // Model API
    public ServiceResponseGetResolverDataset getResolverDataset() {
        // Default response
        ServiceResponseGetResolverDataset response = new ServiceResponseGetResolverDataset();
        response.setApiVersion(ApiCentral.apiVersion);
        response.setHttpStatus(HttpStatus.OK);
        response.setPayload(new ResolverDatasetPayload());
        try {
            response.getPayload().setNamespaces(namespaceApiService.getNamespaceTreeDownToLeaves());
        } catch (RuntimeException e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
}
