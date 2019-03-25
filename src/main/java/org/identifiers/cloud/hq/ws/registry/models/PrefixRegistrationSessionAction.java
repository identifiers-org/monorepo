package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-22 16:12
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface defines the contract for an action, usually taken upon a prefix registration session completion.
 *
 */
public interface PrefixRegistrationSessionAction {

    // Having the report kind of makes sense, as it leaves the exception to true exceptional circumstances. Although it
    // doesn't stop being just another approach experiment...
    PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException;
}
