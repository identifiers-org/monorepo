package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-15 10:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Attending a prefix registration request involves registering the request itself, making possible ammendments to it,
 * possibly associating comments to the request, and closing the request by either accepting it or rejecting it.
 *
 * A prefix registration request management service implements this operations to support this workflow, and it follows
 * a strategy pattern.
 *
 */
public interface PrefixRegistrationRequestManagementService {
    // TODO

    /**
     * This method starts the prefix registration process for the given prefix registration request.
     *
     * In the current iteration of this microservice it uses the prefix registration request model from the API straight
     * away, which is ok, but it may be refactored later, including some model transformation code.
     * @param request the prefix registration requests to use for starting the process
     * @throws PrefixRegistrationRequestManagementServiceException
     */
    void startRequest(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestManagementServiceException;
}
