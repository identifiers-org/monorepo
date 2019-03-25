package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-25 12:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * Composite action to perform upon prefix registration session completion
 */
@Component
@Slf4j
@Qualifier("PrefixRegistrationSessionActionAcceptance")
public class PrefixRegistrationSessionActionAcceptance implements PrefixRegistrationSessionAction {
    // This is one of things of the inversion of control (IOC) that I may not be getting quite right and I hope I get
    // better on it in the future

    @Autowired
    private PrefixRegistrationSessionActionLogger actionLogger;

    private List<PrefixRegistrationSessionAction> buildActionSequence() {
        // TODO
        // TODO - Right now we just log the closing of the prefix registration session, but in the future there will be
        //  notifications and other actions triggered by an accepted prefix registration request
        return Arrays.asList(actionLogger);
    }

    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        PrefixRegistrationSessionActionReport report = new PrefixRegistrationSessionActionReport();
        String messagePrefix = String.format("ACCEPTANCE ACTION for prefix registration session " +
                        "with ID '%d', for prefix '%s', ",
                session.getId(),
                session.getPrefixRegistrationRequest().getRequestedPrefix());
        // TODO Refactor this out in future iterations
        try {
            // If an subaction is not successful, should we stop or keep going?
            // For this iteration of the software, we'll just deal with non-critical chains of actions, so we keep going
            List<PrefixRegistrationSessionActionReport> actionReports = buildActionSequence().parallelStream()
                    .map(action -> action.performAction(session))
                    .filter(PrefixRegistrationSessionActionReport::isError)
                    .collect(Collectors.toList());
            // Set own report to error if any of the subactions completed with error
            if (!actionReports.isEmpty()) {
                report.setErrorMessage(String.format("%s, some actions COMPLETED WITH ERRORS", messagePrefix));
                report.setSuccess(false);
            } else {
                report.setAdditionalInformation(String.format("%s, ALL actions SUCCESSFULY COMPLETED", messagePrefix));
            }
            // Report errors from subactions
            actionReports.parallelStream()
                    .forEach(actionReport -> {
                        log.error(actionReport.getErrorMessage());
                    });
        } catch (RuntimeException e) {
            // Some of them may not be capturing exceptions, let's go up to runtime top level
            throw new PrefixRegistrationSessionActionException(String.format("%s, the following error occurred: %s", messagePrefix, e.getMessage()));
        }
        return report;
    }
}
