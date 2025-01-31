package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseDeactivateNamespacePayload;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseReactivateNamespacePayload;
import org.identifiers.cloud.hq.ws.registry.models.NamespaceLifecycleManagementContext;
import org.identifiers.cloud.hq.ws.registry.models.NamespaceLifecycleManagementOperationReport;
import org.identifiers.cloud.hq.ws.registry.models.NamespaceLifecycleManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private NamespaceLifecycleManagementService namespaceLifecycleManagementService;

    // Auth Helper
    @Autowired
    private AuthHelper authHelper;

    // API
    // Helpers
    private void processNamespaceLifecycleManagementOperationReport(ServiceResponse<?> response, NamespaceLifecycleManagementOperationReport report) {
        if (report.isError()) {
            if (report.getNamespace() == null) {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
            } else {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
            response.setErrorMessage(report.getErrorMessage());
        }
    }

    public ServiceResponse<ServiceResponseDeactivateNamespacePayload> deactivateNamespace(long namespaceId) {
        var response = ServiceResponse.of(new ServiceResponseDeactivateNamespacePayload());
        // TODO Get this from Spring Security
        String actor = authHelper.getCurrentUsername();
        String additionalInformation = "--- no additional information specified ---";
        NamespaceLifecycleManagementContext context = namespaceLifecycleManagementService.createEmptyContext();
        NamespaceLifecycleManagementOperationReport report = namespaceLifecycleManagementService.deactivateNamespace(namespaceId, context, actor, additionalInformation);
        processNamespaceLifecycleManagementOperationReport(response, report);
        response.getPayload().setComment(report.getAdditionalInformation());
        return response;
    }

    public ServiceResponse<ServiceResponseReactivateNamespacePayload> reactivateNamespace(long namespaceId) {
        var response = ServiceResponse.of(new ServiceResponseReactivateNamespacePayload());
        // TODO Get this from Spring Security
        String actor = authHelper.getCurrentUsername();
        String additionalInformation = "--- no additional information specified ---";
        NamespaceLifecycleManagementContext context = namespaceLifecycleManagementService.createEmptyContext();
        NamespaceLifecycleManagementOperationReport report = namespaceLifecycleManagementService.reactivateNamespace(namespaceId, context, actor, additionalInformation);
        processNamespaceLifecycleManagementOperationReport(response, report);
        response.getPayload().setComment(report.getAdditionalInformation());
        return response;
    }
}
