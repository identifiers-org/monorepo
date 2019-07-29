package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 00:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Attending a resource registration request involves registering the request itself, making possible amendments to it,
 * possibly associating comments to the request, and closing the request by either accepting it or rejecting it.
 *
 * A resource registration request management service implements these operations to support this workflow, and it follows
 * a strategy pattern.
 */
public interface ResourceRegistrationRequestManagementService {

    /**
     * This method starts the resource registration process for the given resource registration request.
     * @param request the resource registration request to use for starting the process
     * @param actor the actor that has triggered this action
     * @param additionalInformation additional information related to this action
     * @return the resource registration event registered as a consequence of executing this action
     * @throws ResourceRegistrationRequestManagementServiceException
     */
    ResourceRegistrationSessionEventStart startRequest(ResourceRegistrationRequest request,
                                                       String actor,
                                                       String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;

    /**
     * Amend the resource registration request being processed in the given resource registration session.
     * @param resourceRegistrationSession opened resource registration session where the request is being amended
     * @param amendedRequest amended resource registration request, which is a copy of the latest version of the
     *                       resource registration request, with some changes applied to it
     * @param actor the actor that has triggered this action
     * @param additionalInformation possible additional information related to this action
     * @return the resource registration event registered as a consequence of executing this action
     * @throws ResourceRegistrationRequestManagementServiceException
     */
    ResourceRegistrationSessionEventAmend amendRequest(ResourceRegistrationSession resourceRegistrationSession,
                                                       ResourceRegistrationRequest amendedRequest,
                                                       String actor,
                                                       String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;

    ResourceRegistrationSessionEventComment commentRequest(ResourceRegistrationSession resourceRegistrationSession,
                                                           String comment,
                                                           String actor,
                                                           String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;

    ResourceRegistrationSessionEventReject rejectRequest(ResourceRegistrationSession resourceRegistrationSession,
                                                         String rejectionReason,
                                                         String actor,
                                                         String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;

    ResourceRegistrationSessionEventAccept acceptRequest(ResourceRegistrationSession resourceRegistrationSession,
                                                         String acceptanceReason,
                                                         String actor,
                                                         String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;
    // TODO
}
