package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.slf4j.Logger;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-06 03:41
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This represents a composite action that is made of running a sequence of actions
 */
public interface ResourceRegistrationSessionCompositeSequenceAction extends ResourceRegistrationSessionAction {
    Logger getLogger();
    String getActionName();
    List<ResourceRegistrationSessionAction> buildActionSequence();

    @Override
    default ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        ResourceRegistrationSessionActionReport report = new ResourceRegistrationSessionActionReport();
        String messagePrefix = String.format("%s ACTION for resource registration session with ID '%d', for provider name '%s'", getActionName(), session.getId(), session.getResourceRegistrationRequest().getProviderName());
        try {
            // In this first implementation of the action, we will keep going upon error in any of the subactions, as these actions are not critical, but optional
            List<ResourceRegistrationSessionActionReport> actionErrorReports = buildActionSequence().parallelStream()
                    .map(action -> action.performAction(session))
                    .filter(ResourceRegistrationSessionActionReport::isError)
                    .toList();
            // Set own action report consistent with reports from subactions
            if (!actionErrorReports.isEmpty()) {
                report.setErrorMessage(String.format("%s, some actions COMPLETED WITH ERRORS", messagePrefix));
                report.setSuccess(false);
            } else {
                report.setAdditionalInformation(String.format("%s, ALL actions SUCCESSFULY COMPLETED", messagePrefix));
            }
            // Log error reports
            actionErrorReports.parallelStream().forEach(actionErrorReport -> getLogger().error(actionErrorReport.getErrorMessage()));
        } catch (RuntimeException e) {
            // Some actions may not be capturing exceptions, let's go up to runtime top level
            throw new ResourceRegistrationSessionActionException(String.format("%s, the following error occurred: %s", messagePrefix, e.getMessage()), e);
        }
        return report;
    }
}
