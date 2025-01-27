package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-15 01:17
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface represents the contract met by any ROR API broker / service
 */
public interface RorOrgApiService {
    // TODO
    Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException;
}
