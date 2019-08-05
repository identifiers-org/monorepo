package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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
 * <p>
 * Composite action to perform upon prefix registration session completion
 */
@Component
@Slf4j
@Qualifier("PrefixRegistrationSessionActionAcceptance")
public class PrefixRegistrationSessionActionAcceptance implements PrefixRegistrationSessionCompositeSequenceAction {
    // This is one of things of the inversion of control (IOC) that I may not be getting quite right and I hope I get
    // better on it in the future

    @Autowired
    private PrefixRegistrationSessionActionLogger actionLogger;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "ACCEPTANCE";
    }

    @Override
    public List<PrefixRegistrationSessionAction> buildActionSequence() {
        // TODO
        // TODO - Right now we just log the closing of the prefix registration session, but in the future there will be
        //  notifications and other actions triggered by an accepted prefix registration request
        return Arrays.asList(actionLogger);
    }

}
