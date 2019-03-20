package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSessionEvent;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-20 11:53
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class PrefixRegistrationRequestManagementServiceSimpleWorkflow implements PrefixRegistrationRequestManagementService {
    @Override
    public PrefixRegistrationSessionEvent startRequest(ServiceRequestRegisterPrefixPayload request, String actor,
                                                       String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        return null;
    }

    @Override
    public PrefixRegistrationSessionEvent amendRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                       PrefixRegistrationRequest amendedRequest, String actor,
                                                       String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        return null;
    }

    @Override
    public PrefixRegistrationSessionEvent commentRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                         String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        return null;
    }

    @Override
    public PrefixRegistrationSessionEvent rejectRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                        String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        return null;
    }

    @Override
    public PrefixRegistrationSessionEvent acceptRequest(PrefixRegistrationSession prefixRegistrationSession, String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        return null;
    }
}
