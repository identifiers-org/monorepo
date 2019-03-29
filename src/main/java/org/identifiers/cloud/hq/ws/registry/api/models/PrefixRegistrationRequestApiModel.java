package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEvent;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseRegisterPrefixSessionEventPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.models.PrefixRegistrationRequestManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.ApiDataModelHelper;
import org.identifiers.cloud.hq.ws.registry.models.validators.PrefixRegistrationRequestValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Prefix Registration Request Management Service
    @Autowired
    private PrefixRegistrationRequestManagementService prefixRegistrationRequestManagementService;

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
        boolean isValid = false;
        // Determine who is the actor of this
        String actor = "ANONYMOUS";
        // Possible additional information
        String additionalInformation = "Source, Open API for prefix registration request submission";
        // Run request validation
        try {
            isValid = validatorStrategy.validate(request.getPayload());
        } catch (RuntimeException e) {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setErrorMessage(String.format("INVALID Prefix registration request due to '%s'", e.getMessage()));
            String requestedPrefix = "--- NO PREFIX SPECIFIED ---";
            if ((request.getPayload() != null) && (request.getPayload().getRequestedPrefix() != null)) {
                requestedPrefix = request.getPayload().getRequestedPrefix();
            }
        }
        if (isValid) {
            // Translate data model
            PrefixRegistrationRequest prefixRegistrationRequest =
                    ApiDataModelHelper.getPrefixRegistrationRequest(request.getPayload());
            // Delegate on the Prefix Registration Request Management Service
            prefixRegistrationRequestManagementService.startRequest(prefixRegistrationRequest,
                    actor,
                    additionalInformation);
        }
        return response;
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
