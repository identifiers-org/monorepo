package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.slf4j.Logger;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-05 14:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This represents a composite action that is made of running a sequence of actions
 */
public interface PrefixRegistrationSessionCompositeSequenceAction extends PrefixRegistrationSessionAction {

    Logger getLogger();
    String getActionName();
    List<PrefixRegistrationSessionAction> buildActionSequence();

    @Override
    default PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        PrefixRegistrationSessionActionReport report = new PrefixRegistrationSessionActionReport();
        String messagePrefix = String.format("'%s' ACTION for prefix registration session " +
                        "with ID '%d', for prefix '%s'",
                getActionName(),
                session.getId(),
                session.getPrefixRegistrationRequest().getRequestedPrefix());
        // TODO Refactor this out in future iterations
        try {
            // If a subaction is not successful, should we stop or keep going?
            // For this iteration of the software, we'll just deal with non-critical chains of actions, so we keep going
            List<PrefixRegistrationSessionActionReport> actionReports = buildActionSequence().parallelStream()
                    .map(action -> action.performAction(session))
                    .filter(PrefixRegistrationSessionActionReport::isError)
                    .toList();
            // Set own report to error if any of the subactions completed with error
            if (!actionReports.isEmpty()) {
                report.setErrorMessage(String.format("%s, some actions COMPLETED WITH ERRORS", messagePrefix));
                report.setSuccess(false);
            } else {
                report.setAdditionalInformation(String.format("%s, ALL actions SUCCESSFULY COMPLETED", messagePrefix));
            }
            // Report errors from subactions
            actionReports.parallelStream()
                    .forEach(actionReport ->
                            getLogger().error(actionReport.getErrorMessage())
                    );
        } catch (RuntimeException e) {
            // Some of them may not be capturing exceptions, let's go up to runtime top level
            throw new PrefixRegistrationSessionActionException(String.format("%s, the following error occurred: %s", messagePrefix, e.getMessage()));
        }
        return report;
    }
}
