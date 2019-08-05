package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-05 11:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Slf4j
public class PrefixRegistrationSessionActionNotifierEmailCuratorStart implements PrefixRegistrationSessionAction {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // Configuration

    @Autowired
    private JavaMailSender javaMailSender;

    // Helper
    @Retryable(maxAttempts = MAIL_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = MAIL_REQUEST_RETRY_BACK_OFF_PERIOD))
    private void doSendEmail() {
        // TODO
    }

    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        return null;
    }
}
