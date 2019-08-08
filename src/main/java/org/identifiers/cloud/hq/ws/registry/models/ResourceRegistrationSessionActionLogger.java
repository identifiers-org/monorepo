package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationSessionEventAcceptRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationSessionEventRejectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 16:45
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This action is about logging the fact that a resource registration session has been closed.
 */
@Component
@Slf4j
@Qualifier("ResourceRegistrationSessionActionLogger")
public class ResourceRegistrationSessionActionLogger implements ResourceRegistrationSessionAction {
    // Repositories
    @Autowired
    private ResourceRegistrationSessionEventAcceptRepository resourceRegistrationSessionEventAcceptRepository;
    @Autowired
    private ResourceRegistrationSessionEventRejectRepository resourceRegistrationSessionEventRejectRepository;

    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        // Default report
        ResourceRegistrationSessionActionReport report = new ResourceRegistrationSessionActionReport();
        String logMessagePrefix = String.format("Resource Registration Session with ID '%d', for resource name '%s' - ", session.getId(), session.getResourceRegistrationRequest().getProviderName());
        String message = "";
        if (resourceRegistrationSessionEventRejectRepository.findByResourceRegistrationSessionId(session.getId()) != null) {
            message = String.format("%s has been CLOSED as REJECTED", logMessagePrefix);
            report.setAdditionalInformation(message);
            log.warn(message);
        } else if (resourceRegistrationSessionEventAcceptRepository.findByResourceRegistrationSessionId(session.getId()) != null) {
            message = String.format("%s has been CLOSED as ACCEPTED", logMessagePrefix);
            report.setAdditionalInformation(message);
            log.info(message);
        } else {
            message = String.format("%s IS OPEN", logMessagePrefix);
            report.setErrorMessage(message);
            report.setSuccess(false);
            log.info(message);
        }
        return report;
    }
}
