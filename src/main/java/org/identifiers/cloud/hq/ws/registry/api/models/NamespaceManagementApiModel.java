package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.responses.*;
import org.identifiers.cloud.hq.ws.registry.models.NamespaceLifecycleManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-01 20:19
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
public class NamespaceManagementApiModel {
    // Services
    private NamespaceLifecycleManagementService namespaceLifecycleManagementService;

    // Helpers
    private <T> void initDefaultResponse(ServiceResponse<T> response, T payload) {
        response.setApiVersion(ApiCentral.apiVersion)
                .setHttpStatus(HttpStatus.OK);
        response.setPayload(payload);
    }

    private ServiceResponseDeactivateNamespace createNamespaceDeactivationDefaultResponse() {
        ServiceResponseDeactivateNamespace response = new ServiceResponseDeactivateNamespace();
        initDefaultResponse(response, new ServiceResponseDeactivateNamespacePayload());
        return response;
    }

    private ServiceResponseReactivateNamespace createNamespaceReactivationDefaultResponse() {
        ServiceResponseReactivateNamespace response = new ServiceResponseReactivateNamespace();
        initDefaultResponse(response, new ServiceResponseReactivateNamespacePayload());
        return response;
    }

    // TODO
}
