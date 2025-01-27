package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.identifiers.cloud.hq.ws.registry.configuration.CommonEmailProperties;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResourceRegistrationSessionActionEmailNotifier
        extends RegistrationSessionActionEmailNotifier
                <ResourceRegistrationSession, ResourceRegistrationSessionActionReport>
        implements ResourceRegistrationSessionAction {

    public ResourceRegistrationSessionActionEmailNotifier(Actions actionToNotify, CommonEmailProperties emailCommons, JavaMailSender javaMailSender, Environment env, ResourceLoader resourceLoader) {
        super(actionToNotify, emailCommons, javaMailSender, env, resourceLoader);
    }

    @Override
    protected String getPrefixFromSession(ResourceRegistrationSession session) {
        return session.getResourceRegistrationRequest().getNamespacePrefix();
    }

    @Override
    protected String getRequesterEmailFromSession(ResourceRegistrationSession session) {
        return session.getResourceRegistrationRequest().getRequesterEmail();
    }

    @Override
    protected String getRequestTypePropPrefix() {
        return "resourcereg";
    }

    public String parseEmailBody(ResourceRegistrationSession session) throws ResourceRegistrationSessionActionException {
        try {
            String bodyTemplate = IOUtils.toString(emailBodyFileResource.getInputStream(), StandardCharsets.UTF_8);
            var request = session.getResourceRegistrationRequest();
            return bodyTemplate
                    .replace(emailCommons.getPlaceholderPrefix(), request.getNamespacePrefix())
                    .replace(emailCommons.getPlaceholderRequesterName(), request.getRequesterName())
                    .replaceAll(emailCommons.getPlaceholderSessionId(), Long.toString(session.getId()))
                    .replace(emailCommons.getPlaceholderResourceName(), request.getProviderName())
                    .replace(emailCommons.getPlaceholderResourceDescription(), request.getProviderDescription())
                    .replace(emailCommons.getPlaceholderEmailSupport(), emailCommons.getEmailAddressSupport())
                    .replace(emailCommons.getPlaceholderRequesterEmail(), request.getRequesterEmail());
        } catch (FileNotFoundException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT LOAD prefix request notification e-mail body template for requester at '%s', due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        } catch (IOException e) {
            throw new PrefixRegistrationSessionActionException(String.format("COULD NOT READ prefix request notification e-mail body template for requester at '%s' due to the following error '%s'", emailBodyFileResource, e.getMessage()), e);
        }
    }

    @Override
    protected ResourceRegistrationSessionActionReport newReport() {
        return new ResourceRegistrationSessionActionReport();
    }
}
