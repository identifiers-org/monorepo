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
@RestController(value = "/prefixRegistrationApi")
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
    // TODO
}
