package org.identifiers.cloud.hq.ws.registry.api.data.helpers;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Location;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.helpers
 * Timestamp: 2018-10-17 12:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data helper in the context of the Resolution API data models
 */
public class ResolutionApiHelper {
    // NOTE - I don't totally like to have two models with the same name, as it makes the coding more prone to
    // making mistakes, but, at the same time, this is the meaning of it, and that's why we have packages,
    // right?

    public static Location getLocationFrom(org.identifiers.cloud.hq.ws.registry.data.models.Location location) {
        return new Location().setCountryCode(location.getCountryCode()).setCountryName(location.getCountryName());
    }
}
