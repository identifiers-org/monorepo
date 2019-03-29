package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixSessionEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-03-28 15:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class PrefixRegistrationRequestApiModel {
    // TODO

    public ResponseEntity<?> registerPrefix(ServiceRequestRegisterPrefix request) {
        // TODO 
        return new ResponseEntity<>("registerPrefix()", HttpStatus.OK);
    }

    // TODO - Amend prefix registration request
    public ResponseEntity<?> amendPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        // TODO
        return new ResponseEntity<>("amendPrefixRegistrationRequest()", HttpStatus.OK);
    }

    // TODO - Comment on prefix registration request
    public ResponseEntity<?> commentPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        // TODO
        return new ResponseEntity<>("commentPrefixRegistrationRequest()", HttpStatus.OK);
    }

    // TODO - Reject prefix registration request
    public ResponseEntity<?> rejectPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        // TODO
        return new ResponseEntity<>("rejectPrefixRegistrationRequest()", HttpStatus.OK);
    }

    // TODO - Accept prefix registration request
    public ResponseEntity<?> acceptPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        // TODO
        return new ResponseEntity<>("acceptPrefixRegistrationRequest()", HttpStatus.OK);
    }
}
