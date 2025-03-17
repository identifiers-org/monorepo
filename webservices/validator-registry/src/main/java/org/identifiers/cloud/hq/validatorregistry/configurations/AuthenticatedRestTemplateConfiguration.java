package org.identifiers.cloud.hq.validatorregistry.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
public class AuthenticatedRestTemplateConfiguration {
    @Bean("restTemplateExternal")
    RestTemplate restTemplateExternal(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.version}") String appVersion,
            @Value("${java.version}") String javaVersion,
            @Value("${app.contact}") String appContact
    ) {
        var idorgAgentStr = String.format(
                "IdorgRegistryValidator/%s (%s) Java-http-client/%s",
                javaVersion, appContact, appVersion);
        return restTemplateBuilder
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(USER_AGENT, idorgAgentStr)
                .build();
    }

    @Bean
    @Profile("authenabled")
    OAuth2AuthorizedClientManager authorizedClientManager(
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Bean("restTemplateInternalAuthEnabled")
    @Profile("!authenabled")
    public RestTemplate restTemplateInternalAuthEnabledDev(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setReadTimeout(Duration.ofMinutes(5))
                .setConnectTimeout(Duration.ofSeconds(1))
                .build();
    }

    @Bean
    @Profile("authenabled")
    public RestTemplate restTemplateInternalAuthEnabled(
            RestTemplateBuilder restTemplateBuilder,
            OAuth2AuthorizedClientManager authorizedClientManager,
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        var interceptor = new OAuthClientCredentialsRestTemplateInterceptor(authorizedClientManager, clientRegistration);
        return restTemplateBuilder
                .interceptors(interceptor)
                .setReadTimeout(Duration.ofMinutes(5))
                .setConnectTimeout(Duration.ofSeconds(1))
                .build();
    }
}
