package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-05 11:13
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
public class PrefixRegistrationSessionActionNotifierEmailAcceptance implements PrefixRegistrationSessionAction {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // Configuration
    // E-mail general configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.from}")
    private String emailSender;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.replyto}")
    private String emailReplyTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.acceptance.cc}")
    private String emailCc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.acceptance.cco}")
    private String emailBcc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.acceptance.subject}")
    private String emailSubject;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.acceptance.body.filename}")
    private Resource emailBodyFileResource;
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
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.donotuse}")
    private String placeholderDoNotUse;

    final JavaMailSender javaMailSender;
    public PrefixRegistrationSessionActionNotifierEmailAcceptance(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Helpers
    private String parseEmailSubject(PrefixRegistrationSession session) {
        return emailSubject.replace(placeholderPrefix, session.getPrefixRegistrationRequest().getRequestedPrefix());
    }

    private String parseEmailBody(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        try {
            String bodyTemplate = IOUtils.toString(emailBodyFileResource.getInputStream(), StandardCharsets.UTF_8);
            return bodyTemplate
                    .replace(placeholderPrefix, session.getPrefixRegistrationRequest().getRequestedPrefix())
                    .replace(placeholderPrefixName, session.getPrefixRegistrationRequest().getName())
                    .replace(placeholderPrefixDescription, session.getPrefixRegistrationRequest().getDescription())
                    .replace(placeholderRequesterName, session.getPrefixRegistrationRequest().getRequesterName());
        } catch (FileNotFoundException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT LOAD prefix request notification e-mail body template for requester at '%s', due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        } catch (IOException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT READ prefix request notification e-mail body template for requester at '%s' due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        }
    }

    // TODO Think about a possible refactoring once the mission of the nontifiers is clear
    // Interface
    @Retryable(maxAttempts = MAIL_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = MAIL_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public PrefixRegistrationSessionActionReport performAction(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        PrefixRegistrationSessionActionReport report = new PrefixRegistrationSessionActionReport();
        // Get a plain text message
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        // Set message parameters
        emailMessage.setFrom(emailSender);
        emailMessage.setReplyTo(emailReplyTo);
        emailMessage.setTo(session.getPrefixRegistrationRequest().getRequesterEmail());
        if (!emailCc.equals(placeholderDoNotUse)) emailMessage.setCc(emailCc.split(","));
        if (!emailBcc.equals(placeholderDoNotUse)) emailMessage.setBcc(emailBcc.split(","));
        emailMessage.setSubject(parseEmailSubject(session));
        emailMessage.setText(parseEmailBody(session));
        javaMailSender.send(emailMessage);
        // TODO It would be nice to set something on the report
        return report;
    }
}
