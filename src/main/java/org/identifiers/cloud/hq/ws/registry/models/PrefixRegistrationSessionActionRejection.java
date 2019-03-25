package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-25 12:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class PrefixRegistrationSessionActionRejection implements PrefixRegistrationSessionAction {
    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        return null;
    }
}
