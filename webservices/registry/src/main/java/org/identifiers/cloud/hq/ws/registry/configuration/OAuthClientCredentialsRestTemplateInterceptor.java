package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;
    private final ClientRegistration clientRegistration;

    @Override
    public @NotNull ClientHttpResponse intercept(@NotNull HttpRequest request,
                                                 byte @NotNull [] body,
                                                 @NotNull ClientHttpRequestExecution execution) throws IOException {
        String registrationId = clientRegistration.getRegistrationId();
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(registrationId)
                .principal(registrationId+" RestTemplate Oauth2")
                .build();
        OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);
        if (isNull(client)) {
            throw new IllegalStateException("client credentials flow on " + registrationId + " failed, client is null");
        }

        String token = client.getAccessToken().getTokenValue();
        log.debug("Token {}", token);
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return execution.execute(request, body);
    }
}

