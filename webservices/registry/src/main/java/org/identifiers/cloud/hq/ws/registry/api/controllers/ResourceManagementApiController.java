package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.*;
import org.identifiers.cloud.hq.ws.registry.api.models.ResourceManagementApiModel;
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
public class ResourceManagementApiController {

    @Autowired
    private ResourceManagementApiModel model;


    // --- Resource Registration Request Management ---
    // Resource Registration Request
    @PostMapping(value = "/registerResource")
    public ResponseEntity<?> registerResource(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.registerResource(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // Resource Registration Session, amend
    @PostMapping(value = "/amendResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> amendResourceRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        var response = model.amendResourceRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // Resource Registration Session, comment
    @PostMapping(value = "/commentResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> commentResourceRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        var response = model.commentResourceRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // Resource Registration Session, reject
    @PostMapping(value = "/rejectResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> rejectResourceRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        var response = model.rejectResourceRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // Resource Registration Session, accept
    @PostMapping(value = "/acceptResourceRegistrationRequest/{sessionId}")
    public ResponseEntity<?> acceptResourceRegistrationRequest(@PathVariable long sessionId,
                            @RequestBody ServiceRequest<ServiceRequestRegisterResourceSessionEventPayload> request) {
        var response = model.acceptResourceRegistrationRequest(sessionId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // --- Resource Registration Request Validation ---
    @PostMapping(value = "/validateNamespacePrefix")
    public ResponseEntity<?> validateNamespacePrefix(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateNamespacePrefix(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderHomeUrl")
    public ResponseEntity<?> validateProviderHomeUrl(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderName")
    public ResponseEntity<?> validateProviderName(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderDescription")
    public ResponseEntity<?> validateProviderDescription(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderLocation")
    public ResponseEntity<?> validateProviderLocation(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderCode")
    public ResponseEntity<?> validateProviderCode(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderCode(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionName")
    public ResponseEntity<?> validateInstitutionName(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateInstitutionName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionHomeUrl")
    public ResponseEntity<?> validateInstitutionHomeUrl(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateInstitutionHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionDescription")
    public ResponseEntity<?> validateInstitutionDescription(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateInstitutionDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionLocation")
    public ResponseEntity<?> validateInstitutionLocation(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateInstitutionLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderUrlPattern")
    public ResponseEntity<?> validateProviderUrlPattern(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateProviderUrlPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateSampleId")
    public ResponseEntity<?> validateSampleId(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateSampleId(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAdditionalInformation")
    public ResponseEntity<?> validateAdditionalInformation(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateAdditionalInformation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequester")
    public ResponseEntity<?> validateRequester(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateRequester(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterName")
    public ResponseEntity<?> validateRequesterName(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateRequesterName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterEmail")
    public ResponseEntity<?> validateRequesterEmail(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateRequesterEmail(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAuthHelpDescription")
    public ResponseEntity<?> validateAuthHelpDescription(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateAuthHelpDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAuthHelpUrl")
    public ResponseEntity<?> validateAuthHelpUrl(@RequestBody ServiceRequest<ServiceRequestRegisterResourcePayload> request) {
        var response = model.validateAuthHelpUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    // --- Resource Lifecycle Management
    @GetMapping(value = "/deactivateResource/{resourceId}")
    public ResponseEntity<?> deactivateResource(@PathVariable long resourceId) {
        var response = model.deactivateResource(resourceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/reactivateResource/{resourceId}")
    public ResponseEntity<?> reactivateResource(@PathVariable long resourceId,
                                                @RequestBody
                                                ServiceRequest<ServiceRequestReactivateResourcePayload> request) {
        var response = model.reactivateResource(resourceId, request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
