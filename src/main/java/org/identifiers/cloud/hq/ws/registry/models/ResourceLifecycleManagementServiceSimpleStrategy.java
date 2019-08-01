package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 05:53
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ResourceLifecycleManagementServiceSimpleStrategy implements ResourceLifecycleManagementService {
    @Override
    public ResourceLifecycleManagementOperationReport deactivateResource(Resource resource,
                                                                         ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        return null;
    }

    @Override
    public ResourceLifecycleManagementOperationReport reactivateResource(Resource resource, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException {
        return null;
    }
}
