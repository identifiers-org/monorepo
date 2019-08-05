package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
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
@Slf4j
public class PrefixRegistrationSessionActionNotifierEmailCuratorStart implements PrefixRegistrationSessionAction {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // Configuration
    // E-mail general configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.from}")
    private String emailSender;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.prefixreg.start.to}")
    private String emailTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.prefixreg.start.cc}")
    private String emailCc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.prefixreg.start.cco}")
    private String emailBcc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.prefixreg.start.subject}")
    private String emailSubject;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.prefixreg.start.body.filename}")
    private String emailBodyFileName;
    // Placeholders
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefix}")
    private String placeholderPrefix;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.requestername}")
    private String placeholderRequesterName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefixname}")
    private String placeholderPrefixName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefixdescription}")
    private String placeholderPrefixDescription;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.sessionid}")
    private String placeholderSessionId;


    @Autowired
    private JavaMailSender javaMailSender;

    private String parseEmailSubject(PrefixRegistrationSession session) {
        // TODO
        return null;
    }

    private String parseEmailBody(PrefixRegistrationSession session) {
        // TODO
        return null;
    }

    @Retryable(maxAttempts = MAIL_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = MAIL_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        // Get a plain text message
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        // Set message parameters
        emailMessage.setFrom(emailSender);
        emailMessage.setTo(emailTo.split(","));
        emailMessage.setCc(emailCc.split(","));
        emailMessage.setBcc(emailBcc.split(","));
        emailMessage.setSubject(parseEmailSubject(session));
        emailMessage.setText(parseEmailBody(session));
        javaMailSender.send(emailMessage);
        return null;
    }
}
