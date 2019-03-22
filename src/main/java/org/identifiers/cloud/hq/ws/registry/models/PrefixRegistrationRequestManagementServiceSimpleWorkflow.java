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
        // Check there is no 'reject' event
        if (prefixRegistrationSessionEventRejectRepository.findByPrefixRegistrationSessionId(session.getId()) != null) {
            return false;
        }
        // Check there no 'accept' event
        if (prefixRegistrationSessionEventAcceptRepository.findByPrefixRegistrationSessionId(session.getId()) != null) {
            return false;
        }
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
                            request.getRequestedPrefix(), e.getMessage()));
        }
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventAmend amendRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                            PrefixRegistrationRequest amendedRequest, String actor,
                                                            String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // Check that the prefix registration session is open
        if (!isPrefixRegistrationSessionOpen(prefixRegistrationSession)) {
            throw new PrefixRegistrationRequestManagementServiceException("NO amendment requests ACCEPTED on ALREADY CLOSED Prefix Registration Session");
        }
        try {
            // Persist the amended request
            PrefixRegistrationRequest savedRequest = prefixRegistrationRequestRepository.save(amendedRequest);
            // Create the event
            PrefixRegistrationSessionEventAmend eventAmend = new PrefixRegistrationSessionEventAmend();
            // Reference the amended request in the newly created event
            eventAmend.setActor(actor)
                    .setAdditionalInformation(additionalInformation)
                    .setPrefixRegistrationSession(prefixRegistrationSession)
                    .setPrefixRegistrationRequest(savedRequest);
            eventAmend = prefixRegistrationSessionEventAmendRepository.save(eventAmend);
            // Update the prefix registration request referenced at session level
            prefixRegistrationSession.setPrefixRegistrationRequest(amendedRequest);
            prefixRegistrationSessionRepository.save(prefixRegistrationSession);
            // Return the event
            return eventAmend;
        } catch (RuntimeException e) {
            throw new PrefixRegistrationRequestManagementServiceException(
                    String.format("While amending a prefix registration request with amendment prefix '%s', " +
                            "the following error occurred: '%s'", amendedRequest.getRequestedPrefix(), e.getMessage()));
        }
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventComment commentRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                                String comment,
                                                                String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        // Check that the prefix registration session is open
        if (!isPrefixRegistrationSessionOpen(prefixRegistrationSession)) {
            throw new PrefixRegistrationRequestManagementServiceException("NO comment requests ACCEPTED on ALREADY CLOSED Prefix Registration Session");
        }
        try {
            // TODO
            // Create the event
            PrefixRegistrationSessionEventComment eventComment =
                    new PrefixRegistrationSessionEventComment().setComment(comment);
            // Reference the current session prefix registration request
            eventComment.setActor(actor)
                    .setAdditionalInformation(additionalInformation)
                    .setPrefixRegistrationSession(prefixRegistrationSession)
                    .setPrefixRegistrationRequest(prefixRegistrationSession.getPrefixRegistrationRequest());
            // Persist the event
            // Return the event
            return prefixRegistrationSessionEventCommentRepository.save(eventComment);
        } catch (RuntimeException e) {
            // TODO
        }
        return null;
    }

    @Transactional
    @Override
    public PrefixRegistrationSessionEventReject rejectRequest(PrefixRegistrationSession prefixRegistrationSession,
                                                              String actor, String additionalInformation) throws PrefixRegistrationRequestManagementServiceException {
        try {
            // TODO
        } catch (RuntimeException e) {
            // TODO
        }
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
        try {
            // TODO
        } catch (RuntimeException e) {
            // TODO
        }
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
