package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PrefixRegistrationSessionEventAcceptRepository;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PrefixRegistrationSessionEventRejectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-25 11:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This action is about logging the fact that a prefix registration session has been closed.
 */
@Component
@Slf4j
public class PrefixRegistrationSessionActionLogger implements PrefixRegistrationSessionAction {

    @Autowired
    private PrefixRegistrationSessionEventAcceptRepository prefixRegistrationSessionEventAcceptRepository;

    @Autowired
    private PrefixRegistrationSessionEventRejectRepository prefixRegistrationSessionEventRejectRepository;

    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        // Default report
        PrefixRegistrationSessionActionReport report = new PrefixRegistrationSessionActionReport();
        // TODO
        // TODO Check if the session finished in rejection state
        String logMessagePrefix = String.format("Prefix Registration Session with ID '%d', for prefix '%s' - ", session.getId(), session.getPrefixRegistrationRequest().getRequestedPrefix());
        if (prefixRegistrationSessionEventRejectRepository.findByPrefixRegistrationSessionId(session.getId()) != null) {
            // TODO
            log.warn(String.format("%s has CLOSED as REJECTED", logMessagePrefix));
        } else if (prefixRegistrationSessionEventAcceptRepository.findByPrefixRegistrationSessionId(session.getId()) != null) {
            // TODO
            
        } else {
            // TODO
        }
        // TODO Check if the session finished in acceptance state
        return report;
    }
}
