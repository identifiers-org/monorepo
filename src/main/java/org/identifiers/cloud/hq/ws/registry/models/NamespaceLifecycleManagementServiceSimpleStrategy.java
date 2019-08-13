package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

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
    // Repositories
    @Autowired
    private NamespaceRepository namespaceRepository;

    // Helpers
    private NamespaceLifecycleManagementOperationReport createDefaultReport() {
        return (NamespaceLifecycleManagementOperationReport) new NamespaceLifecycleManagementOperationReport().setAdditionalInformation("--- No additional information set ---");
    }

    private Namespace locateNamespace(long namespaceId, NamespaceLifecycleManagementOperationReport report) {
        Optional<Namespace> locatedNamespace = namespaceRepository.findById(namespaceId);
        if (locatedNamespace.isPresent()) {
            return locatedNamespace.get();
        }
        String errorMessage = String.format("Namespace with ID '%d', NOT FOUND", namespaceId);
        report.setErrorMessage(errorMessage);
        report.setSuccess(false);
        log.error(errorMessage);
        return null;
    }

    // Interface
    @Override
    public NamespaceLifecycleManagementContext createEmptyContext() {
        return new NamespaceLifecycleManagementSimpleStrategyContext();
    }

    @Transactional
    @Override
    public NamespaceLifecycleManagementOperationReport deactivateNamespace(long namespaceId,
                                                                           NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException {
        // Create default report
        NamespaceLifecycleManagementOperationReport report = createDefaultReport();
        // Locate namespace
        Namespace namespace = locateNamespace(namespaceId, report);
        if (namespace != null) {
            report.setNamespace(namespace);
            // Check whether the given namespace is active or not
            if (namespace.isDeprecated()) {
               String errorMessage = String.format("Namespace with ID '%d', MIR ID '%s' CANNOT BE DEACTIVATED, because IT ALREADY IS DEPRECATED", namespace.getId(), namespace.getMirId());
               report.setErrorMessage(errorMessage);
               report.setSuccess(false);
               log.error(errorMessage);
            } else {
                // Should we do any cross check on whether there are active resources on the namespace or not? The same
                // way cross checks from resource lifecycle management didn't make sense, they don't make sense either
                // in the other direction
                namespace.setDeprecated(true);
                namespace.setDeprecationDate(new Date(System.currentTimeMillis()));
                String message = String.format("Namespace with ID '%d', MIR ID '%s' SUCCESSFULLY DEPRECATED", namespace.getId(), namespace.getMirId());
                report.setAdditionalInformation(message);
                log.info(message);
            }
        }
        return report;
    }

    @Transactional
    @Override
    public NamespaceLifecycleManagementOperationReport reactivateNamespace(long namespaceId, NamespaceLifecycleManagementContext context, String actor, String additionalInformation) throws NamespaceLifecycleManagementServiceException {
        // Create default report
        NamespaceLifecycleManagementOperationReport report = createDefaultReport();
        // Locate namespace
        Namespace namespace = locateNamespace(namespaceId, report);
        if (namespace != null) {
            report.setNamespace(namespace);
            // Check whether the given namespace is active or not
            if (!namespace.isDeprecated()) {
                String errorMessage = String.format("Namespace with ID '%d', MIR ID '%s' CANNOT BE RE-ACTIVATED, because IT ALREADY IS ACTIVE", namespace.getId(), namespace.getMirId());
                report.setErrorMessage(errorMessage);
                report.setSuccess(false);
                log.error(errorMessage);
            } else {
                // Should we do any cross check on whether there are active resources on the namespace or not? The same
                // way cross checks from resource lifecycle management didn't make sense, they don't make sense either
                // in the other direction
                namespace.setDeprecated(false);
                // By keeping the previous deprecation date, we have information on when was the last time the namespace was deactivated
                String message = String.format("Namespace with ID '%d', MIR ID '%s' SUCCESSFULLY RE-ACTIVATED", namespace.getId(), namespace.getMirId());
                report.setAdditionalInformation(message);
                log.info(message);
            }
        }
        return report;
    }
}
