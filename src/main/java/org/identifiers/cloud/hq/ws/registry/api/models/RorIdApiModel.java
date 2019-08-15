package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.models.RorOrgApiService;
import org.springframework.beans.factory.annotation.Autowired;
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
        // TODO - Fetch the Organization
        // TODO - Transform model
        return null;
    }
}
