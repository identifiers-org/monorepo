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

    ResourceRegistrationSessionEventStart startRequest(ResourceRegistrationRequest request,
                                                       String actor,
                                                       String additionalInformation) throws ResourceRegistrationRequestManagementServiceException;

    ResourceRegistrationSessionEventAmend amendRequest(ResourceRegistrationSession resourceRegistrationSession,
                                                       ResourceRegistrationRequest amendRequest,
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
