package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-25 12:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Composite action to perform upon prefix registration session completion
 */
@Component
@Qualifier("PrefixRegistrationSessionActionAcceptance")
public class PrefixRegistrationSessionActionAcceptance implements PrefixRegistrationSessionAction {
    // This is one of things of the inversion of control (IOC) that I may not be getting quite right and I hope I get
    // better on it in the future

    @Autowired
    private PrefixRegistrationSessionActionLogger actionLogger;

    private List<PrefixRegistrationSessionAction> buildActionSequence() {
        // TODO
        return Arrays.asList(actionLogger);
    }

    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        PrefixRegistrationSessionActionReport report = new PrefixRegistrationSessionActionReport();
        String messagePrefix = String.format("ACCEPTANCE ACTION for prefix registration session " +
                "with ID '%d', for prefix '%s', ",
                session.getId(),
                session.getPrefixRegistrationRequest().getRequestedPrefix());
        try {
            // TODO
        } catch (RuntimeException e) {
            // Some of them may not be capturing exceptions, let's go up to runtime top level
            // TODO
        }
        return report;
    }
}
