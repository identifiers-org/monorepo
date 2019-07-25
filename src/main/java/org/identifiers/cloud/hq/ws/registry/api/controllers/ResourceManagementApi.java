package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.ResourceManagementApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResource;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourceSessionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-07-25 12:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This API is intended for management of resources at a more complex level than just the REST repositories.
 */
@RestController
@RequestMapping("resourceManagementApi")
public class ResourceManagementApi {

    @Autowired
    private ResourceManagementApiModel model;


    // TODO --- Resource Registration Request Management ---
    // TODO Resource Registration Request
    @PostMapping(value = "/registerResource")
    public ResponseEntity<?> registerPrefix(@RequestBody ServiceRequestRegisterResource request) {
//        ServiceResponseRegisterResource response = model.registerResource(request);
//        return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("WORK IN PROGRESS", HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO Resource Registration Session, amend
    @PostMapping(value = "/amendResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> amendPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
//        ServiceResponseRegisterResource response = model.amendResourceRegistrationRequest(sessionId, request);
//        return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("WORK IN PROGRESS", HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO Resource Registration Session, comment
    @PostMapping(value = "/commentResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> commentPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
//        ServiceResponseRegisterResourceSessionEvent response = model.commentResourceRegistrationRequest(sessionId, request);
//        return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("WORK IN PROGRESS", HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO Resource Registration Session, reject
    @PostMapping(value = "/rejectResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> rejectPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
//        ServiceResponseRegisterResourceSessionEvent response = model.rejectResourceRegistrationRequest(sessionId, request);
//        return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("WORK IN PROGRESS", HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO Resource Registration Session, accept
    @PostMapping(value = "/acceptResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> acceptPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
//        ServiceResponseRegisterResourceSessionEvent response = model.acceptResourceRegistrationRequest(sessionId, request);
//        return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("WORK IN PROGRESS", HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO --- Resource Registration Request Validation ---

    // TODO --- Resource Lifecycle Management
    // TODO
}
