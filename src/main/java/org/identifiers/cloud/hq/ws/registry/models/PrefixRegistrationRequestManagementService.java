package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSessionEvent;

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
     * @param actor the actor that has triggered this action
     * @param additionalInformation possible additional information related to this action
     * @return the prefix registration event registered as a consequence of executing this action
     * @throws PrefixRegistrationRequestManagementServiceException
     */
    PrefixRegistrationSessionEvent startRequest(ServiceRequestRegisterPrefixPayload request,
                                                String actor,
                                                String additionalInformation) throws PrefixRegistrationRequestManagementServiceException;

    /**
     * Amend the prefix registration request being processed in the given prefix registration session.
     *
     * @param prefixRegistrationSession opened prefix registration session where the request is being amended
     * @param amendedRequest amended prefix registration request, which is a copy of the latest version of the prefix
     *                       registration requests, with some changes applied to it.
     * @param actor the actor that has triggered this action
     * @param additionalInformation possible additional information related to this action
     * @return the prefix registration event registered as a consequence of executing this action
     * @throws PrefixRegistrationRequestManagementServiceException
     */
    PrefixRegistrationSessionEvent amendRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                PrefixRegistrationRequest amendedRequest,
                                                String actor,
                                                String additionalInformation) throws PrefixRegistrationRequestManagementServiceException;

    // TODO - comment request
    PrefixRegistrationSessionEvent commentRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                  String actor,
                                                  String additionalInformation) throws PrefixRegistrationRequestManagementServiceException;

    // TODO - reject request
    PrefixRegistrationSessionEvent rejectRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                 String actor,
                                                 String additionalInformation) throws PrefixRegistrationRequestManagementServiceException;

    // TODO - accept request
    PrefixRegistrationSessionEvent acceptRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                 String actor,
                                                 String additionalInformation) throws PrefixRegistrationRequestManagementServiceException;
}
