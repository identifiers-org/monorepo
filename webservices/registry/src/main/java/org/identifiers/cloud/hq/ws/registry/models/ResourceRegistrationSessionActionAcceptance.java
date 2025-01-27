package org.identifiers.cloud.hq.ws.registry.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 16:41
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Composite action to perform within the context of a resource registration session that has just been closed by
 * accepting the request
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("ResourceRegistrationSessionActionAcceptance")
public class ResourceRegistrationSessionActionAcceptance implements ResourceRegistrationSessionCompositeSequenceAction {
    // Related actions
    private final ResourceRegistrationSessionActionLogger actionLogger;
    private final ResourceRegistrationSessionActionEmailNotifier resourceAcceptanceEmailNotificationAction;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "ACCEPTANCE";
    }

    // Director
    @Override
    public List<ResourceRegistrationSessionAction> buildActionSequence() {
        // TODO - Right now, we just log the closing of the resource registration session, but in the future there will
        //  be notifications and other actions triggered by an accepted prefix registration request
        return Arrays.asList(
                actionLogger,
                resourceAcceptanceEmailNotificationAction
        );
    }

}
