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
    // NOTE - With this microservice, I'm going to try a different approach when it comes to API exposure and
    // management, instead of using the ServerRequest and ServerResponse objects I modelled for other microservices, I'm
    // just going to return the responses in either simple ways, e.g. when minting an ID, I will return the minted ID,
    // when loading an ID, response and some message... etc. This microservice API is simple enough to give this
    // approach a try out.

    // --- API ---
    // TODO
    public ResponseEntity<?> mintId() {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<?> keepAlive(String mirId) {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<?> loadId(String mirId) {
        // TODO
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
