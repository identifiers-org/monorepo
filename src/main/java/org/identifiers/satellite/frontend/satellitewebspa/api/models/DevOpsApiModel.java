package org.identifiers.satellite.frontend.satellitewebspa.api.models;

import org.identifiers.satellite.frontend.satellitewebspa.models.SpaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.api.models
 * Timestamp: 2019-05-15 13:09
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model bakcks the DevOps API
 */
@Component
public class DevOpsApiModel {

    @Autowired
    private SpaConfiguration spaConfiguration;

    public ResponseEntity<?> getSpaConfiguration() {
        return new ResponseEntity<SpaConfiguration>(spaConfiguration, HttpStatus.OK);
    }
}
