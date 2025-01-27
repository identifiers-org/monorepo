package org.identifiers.cloud.hq.ws.registry.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.configuration.CommonEmailProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public abstract class RegistrationSessionActionEmailNotifier <S, R> {
    protected static final int MAIL_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    protected static final int MAIL_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    protected final CommonEmailProperties emailCommons;
    protected final String emailTo;
    protected final String emailCc;
    protected final String emailBcc;
    protected final String emailSubject;
    protected final Resource emailBodyFileResource;
    protected final JavaMailSender javaMailSender;

    @RequiredArgsConstructor
    public enum Actions {
        ACCEPTANCE("acceptance", "requester"),
        CURATOR_START("start", "curator"),
        REJECTION("rejection", "requester"),
        REQUESTER_START("start", "requester");

        final String action;
        final String target;
    }

    protected RegistrationSessionActionEmailNotifier(Actions actionToNotify, CommonEmailProperties emailCommons,
                                                     JavaMailSender javaMailSender, Environment env,
                                                     ResourceLoader resourceLoader) {
        this.javaMailSender = javaMailSender;
        this.emailCommons = emailCommons;

        String basePropPrefix = "org.identifiers.cloud.hq.ws.registry.notifiers";
        String mailInfoPropPrefix = String.format("%s.%s.%s.%s.", basePropPrefix,
                actionToNotify.target, getRequestTypePropPrefix(), actionToNotify.action);

        emailTo = env.getProperty(mailInfoPropPrefix + "to");
        emailCc = env.getProperty(mailInfoPropPrefix + "cc");
        emailBcc = env.getProperty(mailInfoPropPrefix + "cco");
        emailSubject = env.getRequiredProperty(mailInfoPropPrefix + "subject");
        String resourceLocation = env.getRequiredProperty(mailInfoPropPrefix + "body.filename");
        emailBodyFileResource = resourceLoader.getResource(resourceLocation);
    }


    protected abstract String getPrefixFromSession(S session);
    protected abstract String getRequesterEmailFromSession(S session);
    protected abstract String parseEmailBody(S session);
    protected abstract R newReport();
    protected abstract String getRequestTypePropPrefix();

    // Helpers
    protected String parseEmailSubject(S session) {
        return emailSubject.replace(emailCommons.getPlaceholderPrefix(),
                                    getPrefixFromSession(session));
    }

    @Retryable(maxAttempts = MAIL_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = MAIL_REQUEST_RETRY_BACK_OFF_PERIOD))
    public R performAction(S session) throws PrefixRegistrationSessionActionException {
        R report = newReport();

        SimpleMailMessage emailMessage = getBaseEmailMessage(session);
        emailMessage.setSubject(parseEmailSubject(session));
        emailMessage.setText(parseEmailBody(session));

        try {
            // Avoid risk of rolling back other actions
            //   (e.g. prefix requests) on email error
            javaMailSender.send(emailMessage);
        } catch (MailException ex) {
            var msg = String.format("Failed to send notification mail \"%s\"",
                    emailMessage.getSubject());
            log.error(msg, ex);
        }

        // TODO It would be nice to set something on the report
        return report;
    }

    SimpleMailMessage getBaseEmailMessage(S session) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();

        emailMessage.setFrom(emailCommons.getEmailSender());
        emailMessage.setReplyTo(emailCommons.getEmailReplyTo());
        if (isNotBlank(emailTo)) {
            emailMessage.setTo(emailTo.split(","));
        } else {
            String requesterEmail = getRequesterEmailFromSession(session);
            emailMessage.setTo(requesterEmail);
        }
        if (isNotBlank(emailCc))
            emailMessage.setCc(emailCc.split(","));
        if (isNotBlank(emailBcc))
            emailMessage.setBcc(emailBcc.split(","));
        return emailMessage;
    }
}
