package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.identifiers.cloud.hq.ws.registry.configuration.CommonEmailProperties;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class PrefixRegistrationSessionActionEmailNotifier
        extends RegistrationSessionActionEmailNotifier
            <PrefixRegistrationSession, PrefixRegistrationSessionActionReport>
        implements PrefixRegistrationSessionAction {

    public PrefixRegistrationSessionActionEmailNotifier(Actions actionToNotify, CommonEmailProperties emailCommons,
                                                        JavaMailSender javaMailSender, Environment env,
                                                        ResourceLoader resourceLoader) {
        super(actionToNotify, emailCommons, javaMailSender, env, resourceLoader);
    }

    @Override
    protected String getPrefixFromSession(PrefixRegistrationSession session) {
        return session.getPrefixRegistrationRequest().getRequestedPrefix();
    }

    @Override
    protected String getRequesterEmailFromSession(PrefixRegistrationSession session) {
        return session.getPrefixRegistrationRequest().getRequesterEmail();
    }

    protected String parseEmailBody(PrefixRegistrationSession session) throws PrefixRegistrationSessionActionException {
        try {
            String bodyTemplate = IOUtils.toString(emailBodyFileResource.getInputStream(), StandardCharsets.UTF_8);
            var request = session.getPrefixRegistrationRequest();
            return bodyTemplate
                    .replace(emailCommons.getPlaceholderPrefix(), request.getRequestedPrefix())
                    .replace(emailCommons.getPlaceholderRequesterName(), request.getRequesterName())
                    .replace(emailCommons.getPlaceholderPrefixName(), request.getName())
                    .replace(emailCommons.getPlaceholderPrefixDescription(), request.getDescription())
                    .replaceAll(emailCommons.getPlaceholderSessionId(), Long.toString(session.getId()))
                    .replace(emailCommons.getPlaceholderEmailSupport(), emailCommons.getEmailAddressSupport())
                    .replace(emailCommons.getPlaceholderRequesterEmail(), request.getRequesterEmail());
        } catch (FileNotFoundException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT LOAD prefix request notification e-mail body template for requester at '%s', due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        } catch (IOException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT READ prefix request notification e-mail body template for requester at '%s' due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        }
    }

    @Override
    protected PrefixRegistrationSessionActionReport newReport() {
        return new PrefixRegistrationSessionActionReport();
    }

    @Override
    protected String getRequestTypePropPrefix() {
        return "prefixreg";
    }
}
