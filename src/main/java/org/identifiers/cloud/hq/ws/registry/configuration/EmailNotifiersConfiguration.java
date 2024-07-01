package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.ws.registry.models.PrefixRegistrationSessionActionEmailNotifier;
import org.identifiers.cloud.hq.ws.registry.models.ResourceRegistrationSessionActionEmailNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;

import static org.identifiers.cloud.hq.ws.registry.models.RegistrationSessionActionEmailNotifier.Actions.*;

@Configuration
@RequiredArgsConstructor
public class EmailNotifiersConfiguration {
    @NonNull final JavaMailSender javaMailSender;
    @NonNull final Environment env;
    @NonNull final ResourceLoader resourceLoader;

    @Bean
    PrefixRegistrationSessionActionEmailNotifier prefixRequesterStartEmailNotificationAction(CommonEmailProperties commons) {
        return new PrefixRegistrationSessionActionEmailNotifier(REQUESTER_START, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    PrefixRegistrationSessionActionEmailNotifier prefixAcceptanceEmailNotificationAction(CommonEmailProperties commons) {
        return new PrefixRegistrationSessionActionEmailNotifier(ACCEPTANCE, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    PrefixRegistrationSessionActionEmailNotifier prefixCuratorStartEmailNotificationAction(CommonEmailProperties commons) {
        return new PrefixRegistrationSessionActionEmailNotifier(CURATOR_START, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    PrefixRegistrationSessionActionEmailNotifier prefixRejectionEmailNotificationAction(CommonEmailProperties commons) {
        return new PrefixRegistrationSessionActionEmailNotifier(REJECTION, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    ResourceRegistrationSessionActionEmailNotifier resourceRequesterStartEmailNotificationAction(CommonEmailProperties commons) {
        return new ResourceRegistrationSessionActionEmailNotifier(REQUESTER_START, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    ResourceRegistrationSessionActionEmailNotifier resourceAcceptanceEmailNotificationAction(CommonEmailProperties commons) {
        return new ResourceRegistrationSessionActionEmailNotifier(ACCEPTANCE, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    ResourceRegistrationSessionActionEmailNotifier resourceCuratorStartEmailNotificationAction(CommonEmailProperties commons) {
        return new ResourceRegistrationSessionActionEmailNotifier(CURATOR_START, commons, javaMailSender, env, resourceLoader);
    }

    @Bean
    ResourceRegistrationSessionActionEmailNotifier resourceRejectionEmailNotificationAction(CommonEmailProperties commons) {
        return new ResourceRegistrationSessionActionEmailNotifier(REJECTION, commons, javaMailSender, env, resourceLoader);
    }
}
