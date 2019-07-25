package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.ResourceManagementApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResource;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterResource;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping(value = "/registerPrefix")
    public ResponseEntity<?> registerPrefix(@RequestBody ServiceRequestRegisterResource request) {
        ServiceResponseRegisterResource response = model.registerResource(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/amendPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> amendPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
        ServiceResponseRegisterResource response = model.amendPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/commentPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> commentPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = model.commentPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/rejectPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> rejectPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = model.rejectPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/acceptPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> acceptPrefixRegistrationRequest(@PathVariable long sessionId, @RequestBody ServiceRequestRegisterResourceSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = model.acceptPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    // TODO Resource Registration Request
    // TODO Resource Registration Session, amend
    // TODO Resource Registration Session, comment
    // TODO Resource Registration Session, reject
    // TODO Resource Registration Session, accept

    // TODO --- Resource Registration Request Validation ---

    // TODO --- Resource Lifecycle Management
    // TODO
}
