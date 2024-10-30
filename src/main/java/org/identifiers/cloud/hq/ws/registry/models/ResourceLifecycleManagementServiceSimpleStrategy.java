package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

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

    // Repositories
    @Autowired
    private ResourceRepository resourceRepository;

    // Helpers
    private ResourceLifecycleManagementOperationReport createDefaultReport() {
        return (ResourceLifecycleManagementOperationReport) new ResourceLifecycleManagementOperationReport()
                .setAdditionalInformation("--- No additional information set ---");
    }

    private Resource locateResource(long resourceId, ResourceLifecycleManagementOperationReport report) {
        Optional<Resource> locatedResource = resourceRepository.findById(resourceId);
        if (locatedResource.isPresent()) {
            return locatedResource.get();
        }
        String errorMessage = String.format("Resource with ID '%d', NOT FOUND", resourceId);
        report.setErrorMessage(errorMessage);
        report.setSuccess(false);
        log.error(errorMessage);
        return null;
    }

    // Interface
    @Override
    public ResourceLifecycleManagementServiceSimpleStrategyContext createEmptyContext() {
        return new ResourceLifecycleManagementServiceSimpleStrategyContext();
    }

    @Transactional
    @Override
    public ResourceLifecycleManagementOperationReport deactivateResource(long resourceId,
                                                                         ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        // Create default report
        ResourceLifecycleManagementOperationReport report = createDefaultReport();
        // Locate resource
        Resource resource = locateResource(resourceId, report);
        if (resource != null) {
            report.setResource(resource);
            // Check whether the given resource is active or not
            if (resource.isDeprecated()) {
                String errorMessage = String.format("Resource with ID '%d', MIR ID '%s' CANNOT BE DEACTIVATED, because IT ALREADY IS DEPRECATED", resource.getId(), resource.getMirId());
                report.setErrorMessage(errorMessage);
                report.setSuccess(false);
                log.error(errorMessage);
            } else {
                // Should we also check at namespace level? If we think about it, deactivating a resource in a namespace
                // that is not active makes sense, I may have deactivated the namespace before deactivating the resource,
                // but also, deactivating a resource in an active namespace also makes perfect sense, it is a normal
                // deactivation operation, so the answer is 'no, we don't need cross validation for this operation from the
                // point of view of a namespace'
                // Deprecate the resource
                resource.setDeprecated(true);
                resource.setDeprecationDate(new Date(System.currentTimeMillis()));
//                String newUrlPattern = deprecationUrlTemplate.replace(deprecationPlaceholderMirId, resource.getMirId()).replace(deprecationPlaceholderLuiPattern, patternLui);
//                resource.setUrlPattern(newUrlPattern);
                String message = String.format("Resource with ID '%d', MIR ID '%s' SUCCESSFULY DEPRECATED, its new URL pattern is '%s'", resource.getId(), resource.getMirId(), resource.getUrlPattern());
                report.setAdditionalInformation(message);
                log.info(message);
            }
        }
        return report;
    }

    @Transactional
    @Override
    public ResourceLifecycleManagementOperationReport reactivateResource(long resourceId, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        // Create default report
        ResourceLifecycleManagementOperationReport report = createDefaultReport();
        // Locate resource
        Resource resource = locateResource(resourceId, report);
        // Specialised context
        ResourceLifecycleManagementServiceSimpleStrategyContext operationContext = (ResourceLifecycleManagementServiceSimpleStrategyContext) context;
        if (resource != null) {
            report.setResource(resource);
            // Check whether the given resource is active or not
            if (!resource.isDeprecated()) {
                String errorMessage = String.format("Resource with ID '%d', MIR ID '%s' CANNOT BE ACTIVATED, because IT ALREADY IS ACTIVE", resource.getId(), resource.getMirId());
                report.setErrorMessage(errorMessage);
                report.setSuccess(false);
                log.error(errorMessage);
            } else {
                // Should we do cross validation with a namespace? In case of reactivating a resource within an active
                // namespace, that's normal operation, and if the namespace is not active, it's also a real life situation
                // where we're activating resources before bringing the namespace back to life. This means we don't really
                // need to cross-validate this operation with namespaces
                // TODO Reactivation context validation? We should probably introduce validators in the future, whenever
                //  possible, to make sure the elements in the reactivation context are valid, e.g. the URL pattern, in a
                //  similar way we always do it for prefix and resource registration requests
                // TODO Reactivate the resource
                resource.setUrlPattern(operationContext.getResourceReactivationUrlPattern());
                resource.setDeprecated(false);
                resource.setRenderProtectedLanding(false);
                resource.setDeprecationDate(null);
                resource.setDeprecationStatement(null);
                // By keeping previous deprecation date, we have information on when was the last time the resource was deactivated
                String message = String.format("Resource with ID '%d', MIR ID '%s' SUCCESSFULLY RE-ACTIVATED, its new URL pattern is '%s'", resource.getId(), resource.getMirId(), resource.getUrlPattern());
                report.setAdditionalInformation(message);
                log.info(message);
            }
        }
        return report;
    }
}
