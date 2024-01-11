package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

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

import java.util.*;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.configuration
 * Timestamp: 2019-04-24 12:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Slf4j
@EnableWebSecurity
public class AuthSecurityConfiguration {
    static final String JWT_SCOPE_RESOURCE_ACCESS = "resource_access";

    @Bean
    @Profile("authenabled")
    public SecurityFilterChain filterChain(HttpSecurity http,
        @Value("${org.identifiers.cloud.ws.miridcontroller.endpoint.requiredrole}")
        String actuatorRequiredRole
    ) throws Exception {
        log.info("[CONFIG] (AAA) ENABLED");
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/restApi/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/healthApi/**").permitAll()
                    .requestMatchers("/mirIdApi/mintId").hasAuthority("idMinting")
                    .requestMatchers("/mirIdApi/keepAlive/**").hasAuthority("idKeepingAlive")
                    .requestMatchers("/mirIdApi/loadId/**").hasAuthority("idLoader")
                    .requestMatchers("/mirIdApi/returnId/**").hasAuthority("idReturner")
                    .requestMatchers("/actuator/loggers/**").hasAuthority(actuatorRequiredRole)
                    .requestMatchers("/actuator").hasAuthority(actuatorRequiredRole)
                    .requestMatchers(HttpMethod.GET, "/actuator/health/**").permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/actuator/loggers/**"))
                .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));
        return http.build();
    }


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
    @Profile("!authenabled")
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        log.info("[CONFIG] (AAA) DISABLED");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
