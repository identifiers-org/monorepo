package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEventPayload;
import org.identifiers.cloud.hq.ws.registry.models.validators.PrefixRegistrationRequestValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Prefix registration request validator
    @Autowired
    private PrefixRegistrationRequestValidatorStrategy validatorStrategy;

    // Helpers
    private ServiceResponseRegisterPrefix createRegisterPrefixDefaultResponse() {
        ServiceResponseRegisterPrefix response = new ServiceResponseRegisterPrefix();
        response.setApiVersion(ApiCentral.apiVersion).setHttpStatus(HttpStatus.OK);
        response.setPayload(new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    private ServiceResponseRegisterPrefixSessionEvent createRegisterPrefixSessionEventDefaultResponse() {
        ServiceResponseRegisterPrefixSessionEvent response = new ServiceResponseRegisterPrefixSessionEvent();
        response.setApiVersion(ApiCentral.apiVersion).setHttpStatus(HttpStatus.OK);
        response.setPayload(new ServiceResponseRegisterPrefixSessionEventPayload());
        return response;
    }
    // END - Helpers

    public ServiceResponseRegisterPrefix registerPrefix(ServiceRequestRegisterPrefix request) {
        // Create default response
        ServiceResponseRegisterPrefix response = createRegisterPrefixDefaultResponse();
        // TODO Run request validation
        // TODO Determine who is the actor of this
        // TODO Possible additional information
        // TODO Translate data model
        // TODO Delegate on the Prefix Registration Request Management Service
        return new ResponseEntity<>("registerPrefix()", HttpStatus.OK);
    }

    // TODO - Amend prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent amendPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }

    // TODO - Comment on prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent commentPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }

    // TODO - Reject prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent rejectPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }

    // TODO - Accept prefix registration request
    public ServiceResponseRegisterPrefixSessionEvent acceptPrefixRegistrationRequest(long sessionId, ServiceRequestRegisterPrefixSessionEvent request) {
        ServiceResponseRegisterPrefixSessionEvent response = createRegisterPrefixSessionEventDefaultResponse();
        // TODO
        return response;
    }
}
