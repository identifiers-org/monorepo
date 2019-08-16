package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.helpers.RorDataModelsHelper;
import org.identifiers.cloud.hq.ws.registry.models.RorOrgApiService;
import org.identifiers.cloud.hq.ws.registry.models.RorOrgApiServiceException;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-14 17:14
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class RorIdApiModel {
    @Autowired
    private RorOrgApiService rorOrgApiService;

    public ResponseEntity<?> getInstitutionForRorId(String rorId) {
        try {
            // TODO Check if the institution already is in the registry
            // Fetch if the institution is not in the registry
            Organization organization = rorOrgApiService.getOrganizationDetails(rorId);
            // Model transformation
            return new ResponseEntity<>(RorDataModelsHelper.getInstitutionFromRorOrganization(organization), HttpStatus.OK);
        } catch (RorOrgApiServiceException e) {
            // TODO Refactor this in order to provide more meaningful error codes
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
