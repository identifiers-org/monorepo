package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixSessionEventPayload;
import org.identifiers.cloud.hq.ws.registry.api.models.PrefixRegistrationRequestApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-03-28 15:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This controller supports the API that handles prefix registration requests
 */
@RestController
@RequestMapping("prefixRegistrationApi")
public class PrefixRegistrationRequestApiController {
    // TODO Refactor this into a "Namespace Management API"

    @Autowired
    private PrefixRegistrationRequestApiModel model;

    @PostMapping(value = "/registerPrefix")
    public ResponseEntity<?> registerPrefix(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.registerPrefix(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/amendPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> amendPrefixRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = model.amendPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/commentPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> commentPrefixRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = model.commentPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/rejectPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> rejectPrefixRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = model.rejectPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/acceptPrefixRegistrationRequest/{sessionId}")
    public ResponseEntity<?> acceptPrefixRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterPrefixSessionEventPayload> request) {
        var response = model.acceptPrefixRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
