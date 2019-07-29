package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;

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
public class ResourceRegistrationSessionActionRejection implements ResourceRegistrationSessionAction {
    // Related actions

    // Director


    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        return null;
    }
}
