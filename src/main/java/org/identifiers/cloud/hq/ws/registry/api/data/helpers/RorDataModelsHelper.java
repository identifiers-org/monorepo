package org.identifiers.cloud.hq.ws.registry.api.data.helpers;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Location;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.helpers
 * Timestamp: 2019-08-15 02:07
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This helps with data model transformations to/from ROR API
 */
public class RorDataModelsHelper {
    // ROR Organization -> API Institution data model
    public static Institution getInstitutionFromRorOrganization(Organization organization) {
        Institution institution = new Institution().setName(organization.getName());
        if (!organization.getLinks().isEmpty()) {
            // Just get the first link
            institution.setHomeUrl(organization.getLinks().get(0));
        }
        if (organization.getCountry() != null) {
            if (organization.getCountry().getCountryCode() != null) {
                institution.setLocation(new Location().setCountryCode(organization.getCountry().getCountryCode()));
            }
        }
        institution.setDescription(String.format("Organization information obtained from ROR API using ROR ID '%s'", organization.getId()));
        return institution;
    }
}
