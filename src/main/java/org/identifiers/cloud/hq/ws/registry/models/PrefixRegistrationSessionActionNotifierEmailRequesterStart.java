package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-05 15:18
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class PrefixRegistrationSessionActionNotifierEmailRequesterStart implements PrefixRegistrationSessionAction {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // Configuration
    // E-mail general configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.from}")
    private String emailSender;
    // TODO Prefilled with the default value, in case it is missing
    //@Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.start.to}")
    //private String emailTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.start.cc}")
    private String emailCc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.start.cco}")
    private String emailBcc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.start.subject}")
    private String emailSubject;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.requester.prefixreg.start.body.filename}")
    private String emailBodyFileResource;
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

    // Helpers
    private String parseEmailSubject(PrefixRegistrationSession session) {
        return emailSubject.replace(placeholderPrefix, session.getPrefixRegistrationRequest().getRequestedPrefix());
    }

    private String parseEmailBody(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        try {
            String bodyTemplate = new String(Files.readAllBytes(ResourceUtils.getFile(emailBodyFileResource).toPath()));
            return bodyTemplate
                    .replace(placeholderPrefix, session.getPrefixRegistrationRequest().getRequestedPrefix())
                    .replace(placeholderPrefixName, session.getPrefixRegistrationRequest().getName())
                    .replace(placeholderPrefixDescription, session.getPrefixRegistrationRequest().getDescription())
                    .replace(placeholderRequesterName, session.getPrefixRegistrationRequest().getRequesterName());
        } catch (FileNotFoundException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT LOAD prefix request notification e-mail body template for requester at '%s', due to the following error '%s'", emailBodyFileResource, e.getMessage()));
        } catch (IOException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT READ prefix request notification e-mail body template for requester at '%s' due to the following error '%s'", emailBodyFileResource, e.getMessage()));
        }
    }

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
        emailMessage.setTo(session.getPrefixRegistrationRequest().getRequesterEmail());
        emailMessage.setCc(emailCc.split(","));
        emailMessage.setBcc(emailBcc.split(","));
        emailMessage.setSubject(parseEmailSubject(session));
        emailMessage.setText(parseEmailBody(session));
        javaMailSender.send(emailMessage);
        // TODO It would be nice to set something on the report
        return report;
    }
}
