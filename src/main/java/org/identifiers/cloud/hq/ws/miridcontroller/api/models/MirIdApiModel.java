package org.identifiers.cloud.hq.ws.miridcontroller.api.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.api.models
 * Timestamp: 2019-02-26 11:17
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class MirIdApiModel {
    // TODO
    public ResponseEntity<?> mintId() {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
