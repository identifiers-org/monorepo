package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;
import org.springframework.stereotype.Component;

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

    // Factory methods

    // Interface
    @Override
    public ResourceLifecycleManagementOperationReport deactivateResource(Resource resource,
                                                                         ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        // TODO Create default report
        // TODO Check whether the given resource is active or not
        // TODO Deprecate the resource
        return null;
    }

    @Override
    public ResourceLifecycleManagementOperationReport reactivateResource(Resource resource, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        return null;
    }
}
