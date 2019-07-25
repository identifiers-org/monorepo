package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.PrefixRegistrationRequestValidationApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixValidate;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixValidateRequest;
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
    public ResponseEntity<?> validateName(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateDescription")
    public ResponseEntity<?> validateDescription(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderHomeUrl")
    public ResponseEntity<?> validateProviderHomeUrl(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderName")
    public ResponseEntity<?> validateProviderName(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderDescription")
    public ResponseEntity<?> validateProviderDescription(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderLocation")
    public ResponseEntity<?> validateProviderLocation(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderCode")
    public ResponseEntity<?> validateProviderCode(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderCode(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionName")
    public ResponseEntity<?> validateInstitutionName(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateInstitutionName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionHomeUrl")
    public ResponseEntity<?> validateInstitutionHomeUrl(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateInstitutionHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionDescription")
    public ResponseEntity<?> validateInstitutionDescription(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateInstitutionDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionLocation")
    public ResponseEntity<?> validateInstitutionLocation(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateInstitutionLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequestedPrefix")
    public ResponseEntity<?> validateRequestedPrefix(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateRequestedPrefix(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderUrlPattern")
    public ResponseEntity<?> validateProviderUrlPattern(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateProviderUrlPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateSampleId")
    public ResponseEntity<?> validateSampleId(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateSampleId(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateIdRegexPattern")
    public ResponseEntity<?> validateIdRegexPattern(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateIdRegexPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateReferences")
    public ResponseEntity<?> validateReferences(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateReferences(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAdditionalInformation")
    public ResponseEntity<?> validateAdditionalInformation(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateAdditionalInformation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequester")
    public ResponseEntity<?> validateRequester(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateRequester(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterName")
    public ResponseEntity<?> validateRequesterName(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateRequesterName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequesterEmail")
    public ResponseEntity<?> validateRequesterEmail(@RequestBody ServiceRequestRegisterPrefixValidate request) {
        ServiceResponseRegisterPrefixValidateRequest response = model.validateRequesterEmail(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
