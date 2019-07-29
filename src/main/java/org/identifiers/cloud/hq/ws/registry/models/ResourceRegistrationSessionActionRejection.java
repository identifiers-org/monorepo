package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 17:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Composite action to perform within the context of a resource registration session that has just been closed by
 * rejecting the request
 */
@Component
@Slf4j
@Qualifier("ResourceRegistrationSessionActionRejection")
public class ResourceRegistrationSessionActionRejection implements ResourceRegistrationSessionAction {
    // Related actions
    @Autowired
    private ResourceRegistrationSessionActionLogger actionLogger;

    // Director
    private List<ResourceRegistrationSessionAction> buildActionSequence() {
        // TODO - Right now, we just log the closing of the resource registration session, but in the future there will
        //  be notifications and other actions triggered by an accepted prefix registration request
        return Arrays.asList(actionLogger);
    }

    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        ResourceRegistrationSessionActionReport report = new ResourceRegistrationSessionActionReport();
        String messagePrefix = String.format("REJECTION ACTION for resource registration session with ID '%d', for provider name '%s'", session.getId(), session.getResourceRegistrationRequest().getProviderName());
        // TODO Refactor this when possible joint with ResourceRegistrationSessionActionAcceptance
        try {
            // In this first implementation of the action, we will keep going upon error in any of the subactions, as these actions are not critical, but optional
            List<ResourceRegistrationSessionActionReport> actionErrorReports = buildActionSequence().parallelStream()
                    .map(action -> action.performAction(session))
                    .filter(ResourceRegistrationSessionActionReport::isError)
                    .collect(Collectors.toList());
            // Set own action report consistent with reports from subactions
            if (!actionErrorReports.isEmpty()) {
                report.setErrorMessage(String.format("%s, some actions COMPLETED WITH ERRORS", messagePrefix));
                report.setSuccess(false);
            } else {
                report.setAdditionalInformation(String.format("%s, ALL actions SUCCESSFULY COMPLETED", messagePrefix));
            }
            // Log error reports
            actionErrorReports.parallelStream().forEach(actionErrorReport -> log.error(actionErrorReport.getErrorMessage()));
        } catch (RuntimeException e) {
            // Some of the actions may not be capturing exceptions, let's go up to runtime top level
            throw new ResourceRegistrationSessionActionException(String.format("%s, the following error occurred: %s", messagePrefix, e.getMessage()));
        }
        return report;
    }
}
