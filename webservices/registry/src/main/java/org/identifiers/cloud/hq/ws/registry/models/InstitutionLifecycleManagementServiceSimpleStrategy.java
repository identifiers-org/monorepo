package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.repositories.InstitutionRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-22 11:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class InstitutionLifecycleManagementServiceSimpleStrategy implements InstitutionLifecycleManagementService {
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    @Transactional
    public InstitutionLifecycleMangementOperationReport deleteById(long id, String actor, String additionalInformation) throws InstitutionLifecycleManagementServiceException {
        InstitutionLifecycleMangementOperationReport report = new InstitutionLifecycleMangementOperationReport();
        // Check if the institution exists
        Optional<Institution> institution = institutionRepository.findById(id);
        if (institution.isPresent()) {
            report.setInstitutionFound(true);
            List<Resource> linkedResources = resourceRepository.findAllByInstitutionId(institution.get().getId());
            if (linkedResources.isEmpty()) {
                // It's not in use
                String message = String.format("Institution with ID '%d', name '%s', PERMANENTLY DELETED FROM THE REGISTRY by '%s', additional information '%s'", institution.get().getId(), institution.get().getName(), actor, additionalInformation);
                institutionRepository.delete(institution.get());
                report.setSuccess(true);
                report.setAdditionalInformation(message);
                log.warn(message);
            } else {
                report.setResources(linkedResources);
                report.setSuccess(false);
                String errorMessage = String.format(
                        "Institution with ID '%d', name '%s', IS IN USE by resources with IDs [%s], AND CANNOT BE DELETED by '%s', additional information '%s'",
                        institution.get().getId(), institution.get().getName(),
                        linkedResources.stream().map(resource -> Long.toString(resource.getId()))
                                .collect(Collectors.joining(",")),
                        actor, additionalInformation);
                report.setErrorMessage(errorMessage);
                log.error(errorMessage);
            }
        } else {
            report.setSuccess(false);
            report.setInstitutionFound(false);
            String errorMessage = String.format("Institution with ID '%d', NOT FOUND, on delete request by '%s', additional information '%s'", id, actor, additionalInformation);
            report.setErrorMessage(errorMessage);
        }
        return report;
    }
}
