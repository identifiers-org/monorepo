package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.models.validators.ResourceLifecycleManagementOperationReport;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 05:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface is related to the lifecycle of resources in the registry, which consists of three main stages:
 *  1.- A new resource is born in the registry
 *  2.- A resource is deactivated in the registry
 *  3.- A deactivated resource is brought back to life in the registry
 *
 * The motivation behind writing this interface is coming from stages '2' and '3' mainly, as there already was a
 * resource registration request management service that takes care of stage '1'.
 *
 * This interface is the contract that different resource lifecycle management strategies will commit to.
 *
 * By introducing this interface, we could argue that a refactoring of 'ResourceRegistrationRequestManagementService'
 * implementations should be done, so stage '1' is delegated to this resource lifecycle management service, but that
 * would mix the lifecycle of a resource with all the logic or lifecycle of a resource registration request, and,
 * although stage '1' seems to be the legitimate handler, and it could probably be abstracted from the resource
 * registration management part, the lack of time, resources and man power for this refactoring will leave the resource
 * registration request stack as it is, and devote the lifecycle management service to stages '2' and '3'.
 *
 * This means whatever the strategy is behind this interface, it is focused on stages '2' and '3', i.e. about resources
 * that already are in the registry.
 */
public interface ResourceLifecycleManagementService {

    /**
     * Given an active resource in the registry, perform a deactivation operation
     * @param resource the resource to deactivate
     * @param actor who is performing the action
     * @param additionalInformation additional information related to this operation
     * @return a report on whether the operation was performed successfully or not
     * @throws ResourceLifecycleManagementServiceException
     */
    ResourceLifecycleManagementOperationReport deactivateResource(Resource resource, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException;

    /**
     * Given an deactivated resource in the registry, perform a reactivation operation
     * @param resource
     * @param actor
     * @param additionalInformation
     * @return
     * @throws ResourceLifecycleManagementServiceException
     */
    ResourceLifecycleManagementOperationReport reactivateResource(Resource resource, ResourceLifecycleManagementContext context, String actor, String additionalInformation) throws ResourceLifecycleManagementServiceException;

    // NOTE: It will be useful for implementation strategies to have a factory method for empty
    // ResourceLifecycleManagementContext objects, so the code can look cleaner
}
