package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.configuration
 * Timestamp: 2019-04-25 08:07
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    static final String JWT_SCOPE_RESOURCE_ACCESS = "resource_access";

    @Bean
    @Profile("authenabled")
    @SuppressWarnings("unchecked")
    public JwtAuthenticationConverter customJwtAuthenticationConverter(
            @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
            String clientId
    ) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter((Jwt source) -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (source.hasClaim("realm_access")) {
                Object roles = source.getClaimAsMap("realm_access").getOrDefault("roles", null);
                if (roles instanceof Collection) {
                    for (var role : ((Collection<String>) roles)) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
            }

            if (source.hasClaim(JWT_SCOPE_RESOURCE_ACCESS)) {
                Map<String, Object> resourceAccessAuthorities = source.getClaimAsMap(JWT_SCOPE_RESOURCE_ACCESS);
                if (resourceAccessAuthorities.containsKey(clientId)) {
                    Map<String, Object> resourceAccessForClientId = (Map<String, Object>) resourceAccessAuthorities.get(clientId);
                    Collection<String> relevantRoles = (Collection<String>) resourceAccessForClientId.get("roles");
                    for (var role : relevantRoles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
            }

            return authorities;
        });
        return converter;
    }

    @Bean
    @Profile("authenabled")
    public SecurityFilterChain filterChain(HttpSecurity http,
               @Value("${org.identifiers.cloud.hq.ws.registry.requiredrole}")
               String actuatorRequiredRole) throws Exception {
        log.info("[CONFIG] (AAA) ENABLED");
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/healthApi/**").permitAll()
                // REST Repository - Institutions
                    .requestMatchers(HttpMethod.GET, "/restApi/institutions/**").permitAll()
                    .requestMatchers(HttpMethod.HEAD, "/restApi/institutions/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/restApi/institutions/**").hasAuthority("restApiInstitutionPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/institutions/**").hasAuthority("restApiInstitutionPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/institutions/**").hasAuthority("restApiInstitutionPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/institutions/**").denyAll()
                // REST Repository - Locations
                    .requestMatchers(HttpMethod.GET, "/restApi/locations/**").permitAll()
                    .requestMatchers(HttpMethod.HEAD, "/restApi/locations/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/restApi/locations/**").hasAuthority("restApiLocationPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/locations/**").hasAuthority("restApiLocationPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/locations/**").hasAuthority("restApiLocationPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/locations/**").denyAll()
                // REST Repository - Namespaces
                    .requestMatchers(HttpMethod.GET, "/restApi/namespaces/*/contactPerson/**").hasAuthority("restApiPersonGet")
                    .requestMatchers(HttpMethod.GET, "/restApi/namespaces/**").permitAll()
                    .requestMatchers(HttpMethod.HEAD, "/restApi/namespaces/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/restApi/namespaces/**").hasAuthority("restApiNamespacePost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/namespaces/**").hasAuthority("restApiNamespacePut")
                    .requestMatchers(HttpMethod.PUT, "/restApi/namespaces/*/successor").hasAuthority("restApiNamespaceSuccessorPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/namespaces/**").hasAuthority("restApiNamespacePatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/namespaces/**").denyAll()
                // REST Repository - Namespace Synonyms
                    .requestMatchers(HttpMethod.GET, "/restApi/namespaceSynonyms/**").permitAll()
                    .requestMatchers(HttpMethod.HEAD, "/restApi/namespaceSynonyms/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/restApi/namespaceSynonyms/**").hasAuthority("restApiNamespaceSynonymPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/namespaceSynonyms/**").hasAuthority("restApiNamespaceSynonymPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/namespaceSynonyms/**").hasAuthority("restApiNamespaceSynonymPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/namespaceSynonyms/**").denyAll()
                // REST Repository - Resources
                    .requestMatchers(HttpMethod.GET, "/restApi/resources/*/contactPerson/**").hasAuthority("restApiPersonGet")
                    .requestMatchers(HttpMethod.GET, "/restApi/resources/**").permitAll()
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resources/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/restApi/resources/**").hasAuthority("restApiResourcePost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resources/**").hasAuthority("restApiResourcePut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resources/**").hasAuthority("restApiResourcePatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resources/**").denyAll()
                // REST Repository - Persons
                    .requestMatchers(HttpMethod.GET, "/restApi/persons/**").hasAuthority("restApiPersonGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/persons/**").hasAuthority("restApiPersonHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/persons/**").hasAuthority("restApiPersonPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/persons/**").hasAuthority("restApiPersonPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/persons/**").hasAuthority("restApiPersonPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/persons/**").denyAll()
                // REST Repository - Prefix Registration Requests
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationRequests/**").hasAuthority("restApiPrefixRegistrationRequestGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationRequests/**").hasAuthority("restApiPrefixRegistrationRequestHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationRequests/**").hasAuthority("restApiPrefixRegistrationRequestPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationRequests/**").hasAuthority("restApiPrefixRegistrationRequestPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationRequests/**").hasAuthority("restApiPrefixRegistrationRequestPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationRequests/**").denyAll()
                // REST Repository - Prefix Registration Sessions
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessions/**").hasAuthority("restApiPrefixRegistrationSessionGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessions/**").hasAuthority("restApiPrefixRegistrationSessionHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessions/**").hasAuthority("restApiPrefixRegistrationSessionPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessions/**").hasAuthority("restApiPrefixRegistrationSessionPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessions/**").hasAuthority("restApiPrefixRegistrationSessionPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessions/**").denyAll()
                // REST Repository - Prefix Registration Session Events
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEvents/**").hasAuthority("restApiPrefixRegistrationSessionEventGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEvents/**").hasAuthority("restApiPrefixRegistrationSessionEventHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEvents/**").hasAuthority("restApiPrefixRegistrationSessionEventPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEvents/**").hasAuthority("restApiPrefixRegistrationSessionEventPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEvents/**").hasAuthority("restApiPrefixRegistrationSessionEventPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEvents/**").denyAll()
                // REST Repository - Prefix Registration Session Events - Accept
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEventAccepts/**").hasAuthority("restApiPrefixRegistrationSessionEventAcceptGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEventAccepts/**").hasAuthority("restApiPrefixRegistrationSessionEventAcceptHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEventAccepts/**").hasAuthority("restApiPrefixRegistrationSessionEventAcceptPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEventAccepts/**").hasAuthority("restApiPrefixRegistrationSessionEventAcceptPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEventAccepts/**").hasAuthority("restApiPrefixRegistrationSessionEventAcceptPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEventAccepts/**").denyAll()
                // REST Repository - Prefix Registration Session Events - Amend
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEventAmends/**").hasAuthority("restApiPrefixRegistrationSessionEventAmendGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEventAmends/**").hasAuthority("restApiPrefixRegistrationSessionEventAmendHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEventAmends/**").hasAuthority("restApiPrefixRegistrationSessionEventAmendPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEventAmends/**").hasAuthority("restApiPrefixRegistrationSessionEventAmendPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEventAmends/**").hasAuthority("restApiPrefixRegistrationSessionEventAmendPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEventAmends/**").denyAll()
                // REST Repository - Prefix Registration Session Event - Comment
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEventComments/**").hasAuthority("restApiPrefixRegistrationSessionEventCommentGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEventComments/**").hasAuthority("restApiPrefixRegistrationSessionEventCommentHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEventComments/**").hasAuthority("restApiPrefixRegistrationSessionEventCommentPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEventComments/**").hasAuthority("restApiPrefixRegistrationSessionEventCommentPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEventComments/**").hasAuthority("restApiPrefixRegistrationSessionEventCommentPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEventComments/**").denyAll()
                // REST Repository - Prefix Registration Session Event - Reject
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEventRejects/**").hasAuthority("restApiPrefixRegistrationSessionEventRejectGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEventRejects/**").hasAuthority("restApiPrefixRegistrationSessionEventRejectHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEventRejects/**").hasAuthority("restApiPrefixRegistrationSessionEventRejectPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEventRejects/**").hasAuthority("restApiPrefixRegistrationSessionEventRejectPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEventRejects/**").hasAuthority("restApiPrefixRegistrationSessionEventRejectPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEventRejects/**").denyAll()
                // REST Repository - Prefix Registration Session Event - Start
                    .requestMatchers(HttpMethod.GET, "/restApi/prefixRegistrationSessionEventStarts/**").hasAuthority("restApiPrefixRegistrationSessionEventStartGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/prefixRegistrationSessionEventStarts/**").hasAuthority("restApiPrefixRegistrationSessionEventStartHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/prefixRegistrationSessionEventStarts/**").hasAuthority("restApiPrefixRegistrationSessionEventStartPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/prefixRegistrationSessionEventStarts/**").hasAuthority("restApiPrefixRegistrationSessionEventStartPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/prefixRegistrationSessionEventStarts/**").hasAuthority("restApiPrefixRegistrationSessionEventStartPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/prefixRegistrationSessionEventStarts/**").denyAll()
                // REST Repository - Resource Registration Requests
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationRequests/**").hasAuthority("restApiResourceRegistrationRequestGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationRequests/**").hasAuthority("restApiResourceRegistrationRequestHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationRequests/**").hasAuthority("restApiResourceRegistrationRequestPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationRequests/**").hasAuthority("restApiResourceRegistrationRequestPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationRequests/**").hasAuthority("restApiResourceRegistrationRequestPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationRequests/**").denyAll()
                // REST Repository - Resource Registration Sessions
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessions/**").hasAuthority("restApiResourceRegistrationSessionGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessions/**").hasAuthority("restApiResourceRegistrationSessionHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessions/**").hasAuthority("restApiResourceRegistrationSessionPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessions/**").hasAuthority("restApiResourceRegistrationSessionPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessions/**").hasAuthority("restApiResourceRegistrationSessionPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessions/**").denyAll()
                // REST Repository - Resource Registration Session Events
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEvents/**").hasAuthority("restApiResourceRegistrationSessionEventGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEvents/**").hasAuthority("restApiResourceRegistrationSessionEventHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEvents/**").hasAuthority("restApiResourceRegistrationSessionEventPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEvents/**").hasAuthority("restApiResourceRegistrationSessionEventPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEvents/**").hasAuthority("restApiResourceRegistrationSessionEventPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEvents/**").denyAll()
                // REST Repository - Resource Registration Session Events - Accept
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEventAccepts/**").hasAuthority("restApiResourceRegistrationSessionEventAcceptGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEventAccepts/**").hasAuthority("restApiResourceRegistrationSessionEventAcceptHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEventAccepts/**").hasAuthority("restApiResourceRegistrationSessionEventAcceptPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEventAccepts/**").hasAuthority("restApiResourceRegistrationSessionEventAcceptPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEventAccepts/**").hasAuthority("restApiResourceRegistrationSessionEventAcceptPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEventAccepts/**").denyAll()
                // REST Repository - Resource Registration Session Events - Amend
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEventAmends/**").hasAuthority("restApiResourceRegistrationSessionEventAmendGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEventAmends/**").hasAuthority("restApiResourceRegistrationSessionEventAmendHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEventAmends/**").hasAuthority("restApiResourceRegistrationSessionEventAmendPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEventAmends/**").hasAuthority("restApiResourceRegistrationSessionEventAmendPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEventAmends/**").hasAuthority("restApiResourceRegistrationSessionEventAmendPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEventAmends/**").denyAll()
                // REST Repository - Resource Registration Session Event - Comment
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEventComments/**").hasAuthority("restApiResourceRegistrationSessionEventCommentGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEventComments/**").hasAuthority("restApiResourceRegistrationSessionEventCommentHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEventComments/**").hasAuthority("restApiResourceRegistrationSessionEventCommentPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEventComments/**").hasAuthority("restApiResourceRegistrationSessionEventCommentPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEventComments/**").hasAuthority("restApiResourceRegistrationSessionEventCommentPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEventComments/**").denyAll()
                // REST Repository - Resource Registration Session Event - Reject
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEventRejects/**").hasAuthority("restApiResourceRegistrationSessionEventRejectGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEventRejects/**").hasAuthority("restApiResourceRegistrationSessionEventRejectHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEventRejects/**").hasAuthority("restApiResourceRegistrationSessionEventRejectPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEventRejects/**").hasAuthority("restApiResourceRegistrationSessionEventRejectPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEventRejects/**").hasAuthority("restApiResourceRegistrationSessionEventRejectPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEventRejects/**").denyAll()
                // REST Repository - Resource Registration Session Event - Start
                    .requestMatchers(HttpMethod.GET, "/restApi/resourceRegistrationSessionEventStarts/**").hasAuthority("restApiResourceRegistrationSessionEventStartGet")
                    .requestMatchers(HttpMethod.HEAD, "/restApi/resourceRegistrationSessionEventStarts/**").hasAuthority("restApiResourceRegistrationSessionEventStartHead")
                    .requestMatchers(HttpMethod.POST, "/restApi/resourceRegistrationSessionEventStarts/**").hasAuthority("restApiResourceRegistrationSessionEventStartPost")
                    .requestMatchers(HttpMethod.PUT, "/restApi/resourceRegistrationSessionEventStarts/**").hasAuthority("restApiResourceRegistrationSessionEventStartPut")
                    .requestMatchers(HttpMethod.PATCH, "/restApi/resourceRegistrationSessionEventStarts/**").hasAuthority("restApiResourceRegistrationSessionEventStartPatch")
                    .requestMatchers(HttpMethod.DELETE, "/restApi/resourceRegistrationSessionEventStarts/**").denyAll()
                // Resolution API
                    .requestMatchers("/resolutionApi/**").permitAll()
                // Semantic API
                    .requestMatchers("/semanticApi/**").permitAll()
                // Prefix Registration API
                    .requestMatchers(HttpMethod.POST, "/prefixRegistrationApi/registerPrefix").permitAll()
                    .requestMatchers("/prefixRegistrationApi/amendPrefixRegistrationRequest/**").hasAuthority("ApiPrefixRegistrationAmendPrefixRegistrationRequest")
                    .requestMatchers("/prefixRegistrationApi/commentPrefixRegistrationRequest/**").hasAuthority("ApiPrefixRegistrationCommentPrefixRegistrationRequest")
                    .requestMatchers("/prefixRegistrationApi/rejectPrefixRegistrationRequest/**").hasAuthority("ApiPrefixRegistrationRejectPrefixRegistrationRequest")
                    .requestMatchers("/prefixRegistrationApi/acceptPrefixRegistrationRequest/**").hasAuthority("ApiPrefixRegistrationAcceptPrefixRegistrationRequest")
                    .requestMatchers(HttpMethod.POST, "/prefixRegistrationApi/validate*").permitAll()
                // Resource Registration API
                    .requestMatchers(HttpMethod.POST, "/resourceManagementApi/registerResource").permitAll()
                    .requestMatchers("/resourceManagementApi/amendResourceRegistrationRequest/**").hasAuthority("ApiResourceManagementAmendResourceRegistrationRequest")
                    .requestMatchers("/resourceManagementApi/commentResourceRegistrationRequest/**").hasAuthority("ApiResourceManagementRegistrationCommentResourceRegistrationRequest")
                    .requestMatchers("/resourceManagementApi/rejectResourceRegistrationRequest/**").hasAuthority("ApiResourceManagementRegistrationRejectResourceRegistrationRequest")
                    .requestMatchers("/resourceManagementApi/acceptResourceRegistrationRequest/**").hasAuthority("ApiResourceManagementRegistrationAcceptResourceRegistrationRequest")
                    .requestMatchers(HttpMethod.POST, "/resourceManagementApi/validate*").permitAll()
                // Resource Lifecycle Management API
                    .requestMatchers(HttpMethod.GET, "/resourceManagementApi/deactivateResource/**").hasAuthority("ApiResourceLifecycleManagementDeactivateResourceRequest")
                    .requestMatchers(HttpMethod.POST, "/resourceManagementApi/reactivateResource/**").hasAuthority("ApiResourceLifecycleManagementReactivateResourceRequest")
                // Namespace Lifecycle Management API
                    .requestMatchers(HttpMethod.GET, "/namespaceManagementApi/deactivateNamespace/**").hasAuthority("ApiNamespaceLifecycleManagementDeactivateNamespaceRequest")
                    .requestMatchers(HttpMethod.GET, "/namespaceManagementApi/reactivateNamespace/**").hasAuthority("ApiNamespaceLifecycleManagementReactivateNamespaceRequest")
                // Institution Management api
                    .requestMatchers(HttpMethod.GET, "/institutionManagementApi/deleteById/**").hasAuthority("ApiInstitutionManagementDeleteByIdRequest")
                // ROR ID API
                    .requestMatchers(HttpMethod.POST, "/rorIdApi/**").permitAll()
                // Schema.org API
                    .requestMatchers("/schemaOrgApi/**").permitAll()
                // Development Auth API
                    .requestMatchers("/devAuthApi/**").permitAll()
                // Development Auth API
                    .requestMatchers("/statistics/**").permitAll()
                // Registry Insight API
                    .requestMatchers(HttpMethod.GET, "/registryInsightApi/getAllNamespacePrefixes").permitAll()
                    .requestMatchers(HttpMethod.GET, "/registryInsightApi/getEbiSearchDataset").permitAll()
                // FAIR API
                    .requestMatchers(HttpMethod.POST, "/fairapi/coverage/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/fairapi/interoperability/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/fairapi/health/**").permitAll()
                // Actuators
                    .requestMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                    .requestMatchers("/actuator").hasAuthority(actuatorRequiredRole)
                    .requestMatchers("/actuator/loggers/**").hasAuthority(actuatorRequiredRole)
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/prefixRegistrationApi/registerPrefix",
                                "/prefixRegistrationApi/validate*",
                                "/resourceManagementApi/registerResource",
                                "/resourceManagementApi/validate*",
                                "/rorIdApi/**",
                                "/fairapi/**",
                                "/actuator/**"))
                .cors(withDefaults())
                .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${org.identifiers.cloud.hq.ws.registry.cors.origin}") String corsOrigins
    ) {
        var allowedCors = List.of(corsOrigins.split(","));

        log.info("[CONFIG] Allowed CORS: {}", allowedCors);

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedCors);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Profile("!authenabled")
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        log.info("[CONFIG] NO AUTH configuration loaded");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .cors(withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
