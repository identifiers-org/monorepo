package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.PrefixRegistrationRequestValidationApiModel;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestValidate;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseValidateRequest;
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
    @Autowired
    private PrefixRegistrationRequestValidationApiModel model;

    @PostMapping(value = "/validateName")
    public ResponseEntity<?> validateName(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @PostMapping(value = "/validateDescription")
    public ResponseEntity<?> validateDescription(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderHomeUrl")
    public ResponseEntity<?> validateProviderHomeUrl(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderName")
    public ResponseEntity<?> validateProviderName(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderDescription")
    public ResponseEntity<?> validateProviderDescription(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderLocation")
    public ResponseEntity<?> validateProviderLocation(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderCode")
    public ResponseEntity<?> validateProviderCode(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderCode(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionName")
    public ResponseEntity<?> validateInstitutionName(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateInstitutionName(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionHomeUrl")
    public ResponseEntity<?> validateInstitutionHomeUrl(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateInstitutionHomeUrl(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionDescription")
    public ResponseEntity<?> validateInstitutionDescription(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateInstitutionDescription(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateInstitutionLocation")
    public ResponseEntity<?> validateInstitutionLocation(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateInstitutionLocation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequestedPrefix")
    public ResponseEntity<?> validateRequestedPrefix(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateRequestedPrefix(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateProviderUrlPattern")
    public ResponseEntity<?> validateProviderUrlPattern(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateProviderUrlPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateSampleId")
    public ResponseEntity<?> validateSampleId(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateSampleId(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateIdRegexPattern")
    public ResponseEntity<?> validateIdRegexPattern(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateIdRegexPattern(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateReferences")
    public ResponseEntity<?> validateReferences(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateReferences(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateAdditionalInformation")
    public ResponseEntity<?> validateAdditionalInformation(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateAdditionalInformation(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/validateRequester")
    public ResponseEntity<?> validateRequester(@RequestBody ServiceRequestValidate request) {
        ServiceResponseValidateRequest response = model.validateRequester(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
