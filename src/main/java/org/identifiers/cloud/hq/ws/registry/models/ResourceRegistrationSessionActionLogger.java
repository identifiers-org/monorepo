package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 16:45
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This action is about logging the fact that a resource registration session has been closed.
 */
public class ResourceRegistrationSessionActionLogger implements ResourceRegistrationSessionAction {
    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        return null;
    }
}
