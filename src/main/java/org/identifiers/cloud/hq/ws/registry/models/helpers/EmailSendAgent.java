package org.identifiers.cloud.hq.ws.registry.models.helpers;

import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.helpers
 * Timestamp: 2019-08-05 11:14
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This implements an e-mail sending agent
 */
@Component
public class EmailSendAgent {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // TODO
}
