package org.identifiers.cloud.hq.ws.miridcontroller.api.models;

import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdHelper;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategy;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
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
    // NOTE - With this microservice, I'm going to try a different approach when it comes to API exposure and
    // management, instead of using the ServerRequest and ServerResponse objects I modelled for other microservices, I'm
    // just going to return the responses in either simple ways, e.g. when minting an ID, I will return the minted ID,
    // when loading an ID, response and some message... etc. This microservice API is simple enough to give this
    // approach a try out.

    @Autowired
    MirIdManagementStrategy mirIdManager;

    // --- API ---
    // TODO
    public ResponseEntity<?> mintId() {
        try {
            return new ResponseEntity<>(MirIdHelper.prettyPrintMirId(mirIdManager.mintId()), HttpStatus.OK);
        } catch (MirIdManagementStrategyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> keepAlive(String mirId) {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<?> loadId(String mirId) {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<?> returnId(String mirId) {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
