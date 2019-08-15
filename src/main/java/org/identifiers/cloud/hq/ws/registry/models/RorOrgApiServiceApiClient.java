package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-15 01:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class RorOrgApiServiceApiClient implements RorOrgApiService {
    // TODO
    @Override
    public Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException {
        return null;
    }
}
