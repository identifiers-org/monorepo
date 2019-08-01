package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 18:53
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
public class NamespaceLifecycleManagementServiceSimpleStrategy implements NamespaceLifecycleManagementService {
    @Override
    public NamespaceLifecycleManagementContext createEmptyContext() {
        return null;
    }

    @Override
    public NamespaceLifecycleManagementOperationReport deactivateNamespace(long namespaceId,
                                                                           NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException {
        return null;
    }

    @Override
    public NamespaceLifecycleManagementOperationReport reactivateNamespace(long namespaceId, NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException {
        return null;
    }
}
