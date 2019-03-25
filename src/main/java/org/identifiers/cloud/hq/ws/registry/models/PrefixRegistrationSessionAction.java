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
    // TODO
    // TODO I'll probably remove the return value, as I think I'm not using it anywhere, and it doesn't seem to make sense
    PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException;
}
