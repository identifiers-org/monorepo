package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 17:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This interface is related to the lifecycle of namespaces in the registry, which consists of three main stages:
 * 1.- A new namespace is born in the registry
 * 2.- A namespace is deactivated in the registry
 * 3.- A deactivated namespace is brought back to life in the registry
 * <p>
 * The motivation behind writing this interface is coming from stages '2' and '3' mainly, as there already was a
 * namespace registration request management service that takes care of stage '1'.
 * <p>
 * This interface is the contract that different namespace lifecycle management strategies will commit to.
 * <p>
 * By introducing this interface, we could argue that a refactoring of 'PrefixRegistrationRequestManagementService'
 * implementations should be done, so tage '1' is delegated to this namespace lifecycle management service, but that
 * would mix the lifecycle of a namespace with all the logic or lifecycle of a prefix (namespace) registration request,
 * and, although stage '1' seems to be the legitimate handler, and it could probably be abstracted from the namespace
 * (prefix) registration management part, the lack of time, resource and man power for this refactoring will leave the
 * namespace registration request stack as it is, and devote the lifecycle management service to stages '2' and '3'.
 * <p>
 * This means whatever the strategy is behind this interface, it is focused on stages '2' and '3', i.e. about
 * namespaces that already are in the registry.
 */
public interface NamespaceLifecycleManagementService {

    /**
     * This is a factory method that will create an empty context specific for the implementation being used
     * @return an empty context
     */
    NamespaceLifecycleManagementContext createEmptyContext();

    NamespaceLifecycleManagementOperationReport deactivateNamespace(long namespaceId, NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException;

    NamespaceLifecycleManagementOperationReport reactivateNamespace(long namespaceId, NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException;
}
