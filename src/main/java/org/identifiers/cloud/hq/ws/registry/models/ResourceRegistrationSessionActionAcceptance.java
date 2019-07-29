package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;

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
public class ResourceRegistrationSessionActionAcceptance implements ResourceRegistrationSessionAction {
    // Related actions
    @Autowired
    private ResourceRegistrationSessionActionLogger actionLogger;
    
    // TODO
    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        return null;
    }
}
