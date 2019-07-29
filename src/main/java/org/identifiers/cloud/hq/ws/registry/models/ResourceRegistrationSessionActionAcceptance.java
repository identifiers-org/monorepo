package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
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
@Component
@Slf4j
@Qualifier("ResourceRegistrationSessionActionAcceptance")
public class ResourceRegistrationSessionActionAcceptance implements ResourceRegistrationSessionAction {
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
        String messagePrefix = String.format("ACCEPTANCE ACTION for resource registration session with ID '%d', for provider name '%s'", session.getId(), session.getResourceRegistrationRequest().getProviderName());
        try {
            // TODO
        } catch (RuntimeException e) {
            // Some of the actions may not be capturing exceptions, let's go up to runtime top level
            throw new ResourceRegistrationSessionActionException(String.format("%s, the following error occurred: %s", messagePrefix, e.getMessage()));
        }
        // TODO
        return null;
    }
}
