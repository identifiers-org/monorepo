package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 05:53
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
public class ResourceLifecycleManagementServiceSimpleStrategy implements ResourceLifecycleManagementService {
    // Config
    @Value("${org.identifiers.cloud.hq.ws.lifecycle.resources.pattern.lui}")
    private String patternLui;
    @Value("${org.identifiers.cloud.hq.ws.lifecycle.resources.deprecation.urltemplate.placeholder.mirid}")
    private String deprecationPlaceholderMirId;
    @Value("${org.identifiers.cloud.hq.ws.lifecycle.resources.deprecation.urltemplate.placeholder.luipattern}")
    private String deprecationPlaceholderLuiPattern;
    @Value("${org.identifiers.cloud.hq.ws.lifecycle.resources.deprecation.urltemplate}")
    private String deprecationUrlTemplate;
    // Factory methods

    // Helpers
    private ResourceLifecycleManagementOperationReport createDefaultReport() {
        return (ResourceLifecycleManagementOperationReport) new ResourceLifecycleManagementOperationReport()
                .setAdditionalInformation("--- No additional information set ---");
    }

    // Interface
    @Transactional
    @Override
    public ResourceLifecycleManagementOperationReport deactivateResource(Resource resource,
                                                                         ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        // Create default report
        ResourceLifecycleManagementOperationReport report = createDefaultReport();
        // Check whether the given resource is active or not
        if (resource.isDeprecated()) {
            String errorMessage = String.format("Resource with ID '%d', MIR ID '%s' CANNOT BE DEACTIVATED, because IT ALREADY IS DEPRECATED", resource.getId(), resource.getMirId());
            report.setErrorMessage(errorMessage);
            report.setSuccess(false);
            log.error(errorMessage);
        } else {
            // Deprecate the resource
            resource.setDeprecated(true);
            resource.setDeprecationDate(new Date(System.currentTimeMillis()));
            String newUrlPattern = deprecationUrlTemplate.replace(deprecationPlaceholderMirId, resource.getMirId()).replace(deprecationPlaceholderLuiPattern, patternLui);
            resource.setUrlPattern(newUrlPattern);
            String message = String.format("Resource with ID '%d', MIR ID '%s' SUCCESSFULY DEPRECATED, its new URL pattern is '%s'", resource.getId(), resource.getMirId(), resource.getUrlPattern());
            report.setAdditionalInformation(message);
            log.info(message);
        }
        return report;
    }

    @Transactional
    @Override
    public ResourceLifecycleManagementOperationReport reactivateResource(Resource resource, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        // TODO Create default report
        // TODO Check whether the given resource is active or not
        // TODO Reactivation context validation?
        // TODO Reactivate the resource
        return null;
    }
}
