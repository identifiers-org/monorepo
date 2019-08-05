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
 * Timestamp: 2019-03-25 12:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
@Qualifier("PrefixRegistrationSessionActionRejection")
public class PrefixRegistrationSessionActionRejection implements PrefixRegistrationSessionCompositeSequenceAction {
    @Autowired
    private PrefixRegistrationSessionActionLogger actionLogger;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "REJECTION";
    }

    public List<PrefixRegistrationSessionAction> buildActionSequence() {
        // TODO
        // TODO - Right now we just log the closing of the prefix registration session, but in the future there will be
        //  notifications and other actions triggered by a rejected prefix registration request
        return Arrays.asList(actionLogger);
    }

}
