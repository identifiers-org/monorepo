package org.identifiers.cloud.hq.ws.registry.models;

import org.apache.commons.io.IOUtils;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-06 04:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class ResourceRegistrationSessionActionNotifierEmailCuratorStart implements ResourceRegistrationSessionAction {
    private static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    // Configuration
    // E-mail general configuration
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.from}")
    private String emailSender;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.replyto}")
    private String emailReplyTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.resourcereg.start.to}")
    private String emailTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.resourcereg.start.cc}")
    private String emailCc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.resourcereg.start.cco}")
    private String emailBcc;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.resourcereg.start.subject}")
    private String emailSubject;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.curator.resourcereg.start.body.filename}")
    private String emailBodyFileResource;
    // Placeholders
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefix}")
    private String placeholderPrefix;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.resource.name}")
    private String placeholderResourceName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.resource.description}")
    private String placeholderResourceDescription;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.requestername}")
    private String placeholderRequesterName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.sessionid}")
    private String placeholderSessionId;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ResourceLoader resourceLoader;

    private String parseEmailSubject(ResourceRegistrationSession session) {
        return emailSubject.replace(placeholderPrefix, session.getResourceRegistrationRequest().getNamespacePrefix());
    }

    private String parseEmailBody(ResourceRegistrationSession session) throws PrefixRegistrationSessionActionException {
        try {
            String bodyTemplate = IOUtils.toString(resourceLoader.getResource(emailBodyFileResource).getInputStream(), Charset.forName("UTF-8"));
            return bodyTemplate
                    .replace(placeholderPrefix, session.getResourceRegistrationRequest().getNamespacePrefix())
                    .replace(placeholderResourceName, session.getResourceRegistrationRequest().getProviderName())
                    .replace(placeholderResourceDescription, session.getResourceRegistrationRequest().getProviderDescription())
                    .replace(placeholderRequesterName, session.getResourceRegistrationRequest().getRequesterName())
                    .replace(placeholderSessionId, Long.toString(session.getId()));
        } catch (FileNotFoundException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT LOAD notification e-mail body template at '%s', due to the following error '%s'", emailBodyFileResource, e.getMessage()));
        } catch (IOException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT READ notification e-mail body template at '%s' due to the following error '%s'", emailBodyFileResource, e.getMessage()));
        }
    }

    @Retryable(maxAttempts = MAIL_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = MAIL_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public ResourceRegistrationSessionActionReport performAction(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        ResourceRegistrationSessionActionReport report = new ResourceRegistrationSessionActionReport();
        // Get a plain text message
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        // Set message parameters
        emailMessage.setFrom(emailSender);
        emailMessage.setReplyTo(emailReplyTo);
        emailMessage.setTo(emailTo.split(","));
        emailMessage.setCc(emailCc.split(","));
        emailMessage.setBcc(emailBcc.split(","));
        emailMessage.setSubject(parseEmailSubject(session));
        emailMessage.setText(parseEmailBody(session));
        javaMailSender.send(emailMessage);
        // TODO It would be nice to set something on the report
        return report;
    }
}
