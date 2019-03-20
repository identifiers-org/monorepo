package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.*;
import org.identifiers.cloud.hq.ws.registry.data.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-20 11:53
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class PrefixRegistrationRequestManagementServiceSimpleWorkflow implements PrefixRegistrationRequestManagementService {

    // Repositories
    @Autowired
    private PrefixRegistrationRequestRepository prefixRegistrationRequestRepository;
    @Autowired
    private PrefixRegistrationSessionRepository prefixRegistrationSessionRepository;
    @Autowired
    private PrefixRegistrationSessionEventStartRepository prefixRegistrationSessionEventStartRepository;
    @Autowired
    private PrefixRegistrationSessionEventAmendRepository prefixRegistrationSessionEventAmendRepository;
    @Autowired
    private PrefixRegistrationSessionEventRejectRepository prefixRegistrationSessionEventRejectRepository;
    @Autowired
    private PrefixRegistrationSessionEventAcceptRepository prefixRegistrationSessionEventAcceptRepository;
    @Autowired
    private PrefixRegistrationSessionEventCommentRepository prefixRegistrationSessionEventCommentRepository;
    // --- END - Repositories

    // Helpers
    private boolean isPrefixRegistrationSessionOpen(PrefixRegistrationSession session) {
        // TODO
        // Check there is no reject event
        
        return true;
    }
    // --- END - Helpers

    @Transactional
    @Override
    public PrefixRegistrationSessionEventStart startRequest(PrefixRegistrationRequest request, String actor,
                                                            String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        try {
            // Persist the given prefix registration request
            PrefixRegistrationRequest savedRequest = prefixRegistrationRequestRepository.save(request);
            // Open a new prefix registration session
            // Set the given prefix registration request
            PrefixRegistrationSession session = new PrefixRegistrationSession().setPrefixRegistrationRequest(savedRequest);
            session = prefixRegistrationSessionRepository.save(session);
            // Create a 'start' event
            PrefixRegistrationSessionEventStart sessionEventStart = new PrefixRegistrationSessionEventStart();
            sessionEventStart.setActor(actor)
                    .setAdditionalInformation(additionalInformation)
                    .setPrefixRegistrationRequest(savedRequest)
                    .setPrefixRegistrationSession(session);
            // Return the event
            return prefixRegistrationSessionEventStartRepository.save(sessionEventStart);
        } catch (RuntimeException e) {
            throw new PrefixRegistrationRequestManagementServiceException(
                    String.format("While starting a prefix registration session for prefix registration request " +
                            "on '%s' prefix, the following error occurred: '%s'",
                            request.getRequestedPrefix(), e.getMessage()))
        }
        return null;
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventAmend amendRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                            PrefixRegistrationRequest amendedRequest, String actor,
                                                            String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // TODO Check that the prefix registration session is open
        // TODO Create the event
        // TODO Persist the amended request
        // TODO Reference the amended request in the newly created event
        // TODO Update the prefix registration request referenced at session level
        // TODO Return the event
        return null;
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventComment commentRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                                String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // TODO Check that the prefix registration session is open
        // TODO Create the event
        // TODO Reference the current session prefix registration request
        // TODO Persist the event
        // TODO Return the event
        return null;
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventReject rejectRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                              String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // TODO Check that the prefix registration session is open
        // TODO Create the event
        // TODO Reference the current session prefix registration request
        // TODO Persist the event
        // Session is considered 'closed' right now
        // TODO Run the 'reject' chain of actions
        // TODO Return the event
        return null;
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventAccept acceptRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                              String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // TODO Check that the prefix registration session is open
        // TODO Create the event
        // TODO Reference the current session prefix registration request
        // TODO Persist the event
        // Session is considered 'closed' right now
        // TODO Run the 'accept' chain of actions
        // TODO Return the event
        return null;
    }
}
