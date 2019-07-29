package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 11:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface defines the contract for an action, usually taken upon a resource registration session completion.
 */
public interface ResourceRegistrationSessionAction {
    // Having the report kind of makes sense, as it leaves the exception to true exceptional circumstances. Although it
    // doesn't stop being just another approach experiment...
    ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException;

}
