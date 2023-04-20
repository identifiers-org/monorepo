package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.configuration
 * Timestamp: 2019-04-24 12:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Profile("authenabled")
@Slf4j
@EnableWebSecurity
public class AuthSecurityConfiguration extends WebSecurityConfigurerAdapter {
    static final String JWT_SCOPE_RESOURCE_ACCESS = "resource_access";

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${org.identifiers.cloud.ws.miridcontroller.endpoint.requiredrole}")
    private String actuatorRequiredRole;

    @PostConstruct
    private void postConstruct() {
        log.info("[CONFIG] (AAA) ENABLED");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO
        // Here's one to the multiple "Copy-and-Paste" replicas of shallow tutorials that can be found on the internet
        // about this. There is no documentation on how to use SpEL with access(), so this has been incredibly
        // frustrating. In addition, "expression based access control" is INCREDIBLY POORLY documented everywhere,
        // including Spring official documentation
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/restApi/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/healthApi/**").permitAll()
                    .antMatchers("/mirIdApi/mintId").access(String.format("isAuthenticated() and (principal?.claims.get('%s') != null) and (principal?.claims['%s'].get('%s') != null) and (principal?.claims['%s']['%s']['roles'].contains('idMinting'))", JWT_SCOPE_RESOURCE_ACCESS, JWT_SCOPE_RESOURCE_ACCESS, clientId, JWT_SCOPE_RESOURCE_ACCESS, clientId))
                    .antMatchers("/mirIdApi/keepAlive/**").access(String.format("isAuthenticated() and (principal?.claims.get('%s') != null) and (principal?.claims['%s'].get('%s') != null) and (principal?.claims['%s']['%s']['roles'].contains('idKeepingAlive'))", JWT_SCOPE_RESOURCE_ACCESS, JWT_SCOPE_RESOURCE_ACCESS, clientId, JWT_SCOPE_RESOURCE_ACCESS, clientId))
                    .antMatchers("/mirIdApi/loadId/**").access(String.format("isAuthenticated() and (principal?.claims.get('%s') != null) and (principal?.claims['%s'].get('%s') != null) and (principal?.claims['%s']['%s']['roles'].contains('idLoader'))", JWT_SCOPE_RESOURCE_ACCESS, JWT_SCOPE_RESOURCE_ACCESS, clientId, JWT_SCOPE_RESOURCE_ACCESS, clientId))
                    .antMatchers("/mirIdApi/returnId/**").access(String.format("isAuthenticated() and (principal?.claims.get('%s') != null) and (principal?.claims['%s'].get('%s') != null) and (principal?.claims['%s']['%s']['roles'].contains('idReturner'))", JWT_SCOPE_RESOURCE_ACCESS, JWT_SCOPE_RESOURCE_ACCESS, clientId, JWT_SCOPE_RESOURCE_ACCESS, clientId))
                    .antMatchers("/actuator/loggers/**").access(String.format("isAuthenticated() and principal?.claims['realm_access']['roles'].contains('%s')", actuatorRequiredRole))
                    .antMatchers("/actuator").access(String.format("isAuthenticated() and principal?.claims['realm_access']['roles'].contains('%s')", actuatorRequiredRole))
                    .antMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                    .anyRequest().denyAll()
                .and()
                    .csrf().ignoringAntMatchers("/actuator/loggers/**")
                .and()
                    .oauth2ResourceServer().jwt();
    }
}
