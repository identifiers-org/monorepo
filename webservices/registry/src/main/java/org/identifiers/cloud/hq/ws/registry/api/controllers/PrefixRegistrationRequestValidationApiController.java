package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.models.PrefixRegistrationRequestValidationApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-03-18 14:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping(value = "prefixRegistrationApi")
public class PrefixRegistrationRequestValidationApiController {
    // TODO Refactor this into a "Namespace Management API

    @Autowired
    private PrefixRegistrationRequestValidationApiModel model;

    @PostMapping(value = "/validateName")
    public ResponseEntity<?> validateName(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateDescription")
    public ResponseEntity<?> validateDescription(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderHomeUrl")
    public ResponseEntity<?> validateProviderHomeUrl(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderName")
    public ResponseEntity<?> validateProviderName(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderDescription")
    public ResponseEntity<?> validateProviderDescription(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderLocation")
    public ResponseEntity<?> validateProviderLocation(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderCode")
    public ResponseEntity<?> validateProviderCode(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderCode(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionName")
    public ResponseEntity<?> validateInstitutionName(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateInstitutionName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionHomeUrl")
    public ResponseEntity<?> validateInstitutionHomeUrl(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateInstitutionHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionDescription")
    public ResponseEntity<?> validateInstitutionDescription(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateInstitutionDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionLocation")
    public ResponseEntity<?> validateInstitutionLocation(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateInstitutionLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequestedPrefix")
    public ResponseEntity<?> validateRequestedPrefix(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateRequestedPrefix(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderUrlPattern")
    public ResponseEntity<?> validateProviderUrlPattern(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateProviderUrlPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateSampleId")
    public ResponseEntity<?> validateSampleId(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateSampleId(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateIdRegexPattern")
    public ResponseEntity<?> validateIdRegexPattern(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateIdRegexPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateReferences")
    public ResponseEntity<?> validateReferences(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateReferences(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAdditionalInformation")
    public ResponseEntity<?> validateAdditionalInformation(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateAdditionalInformation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequester")
    public ResponseEntity<?> validateRequester(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateRequester(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterName")
    public ResponseEntity<?> validateRequesterName(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateRequesterName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterEmail")
    public ResponseEntity<?> validateRequesterEmail(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateRequesterEmail(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAuthHelpDescription")
    public ResponseEntity<?> validateAuthHelpDescription(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateAuthHelpDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAuthHelpUrl")
    public ResponseEntity<?> validateAuthHelpUrl(@RequestBody ServiceRequest<ServiceRequestRegisterPrefixPayload> request) {
        var response = model.validateAuthHelpUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
